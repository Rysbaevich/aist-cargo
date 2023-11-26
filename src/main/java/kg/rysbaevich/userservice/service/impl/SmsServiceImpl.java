package kg.rysbaevich.userservice.service.impl;

import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsTextualMessage;
import jakarta.transaction.Transactional;
import kg.rysbaevich.userservice.exceptions.SmsException;
import kg.rysbaevich.userservice.exceptions.VerificationException;
import kg.rysbaevich.userservice.model.SMS;
import kg.rysbaevich.userservice.model.User;
import kg.rysbaevich.userservice.model.dto.UserDto;
import kg.rysbaevich.userservice.model.request.VerificationRequest;
import kg.rysbaevich.userservice.repository.SmsRepository;
import kg.rysbaevich.userservice.service.SmsService;
import kg.rysbaevich.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${infobip.base.url}")
    private String BASE_URL;

    @Value("${infobip.api.key}")
    private String API_KEY;

    private final UserService userService;
    private final SmsRepository smsRepository;

    @Transactional(dontRollbackOn = NoSuchElementException.class)
    public void send(String phoneNumber) {
        // Create the API client and the Send SMS API instances.
        var apiClient = ApiClient.forApiKey(ApiKey.from(API_KEY))
                .withBaseUrl(BaseUrl.from(BASE_URL))
                .build();
        var sendSmsApi = new SmsApi(apiClient);

        // Create a message to send.
        String verificationCode = createVerificationCode();

        UserDto userDto;
        try {
            userDto = userService.findUserByPhone(phoneNumber);
        } catch (NoSuchElementException e) {
            userDto = userService.save(new UserDto(phoneNumber, false));
        }

        SMS sms = new SMS();
        sms.setUser(new User(userDto));
        sms.setCode(verificationCode);
        sms.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        var smsMessage = new SmsTextualMessage()
                .addDestinationsItem(new SmsDestination().to(phoneNumber))
                .text("Уважаемый клиент Ваш код верификации для приложения AistCargo: " + verificationCode);

        // Create a send message request.
        var smsMessageRequest = new SmsAdvancedTextualRequest().messages(Collections.singletonList(smsMessage));

        try {
            // Send the message.
            var smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest).execute();
            log.info("Response body: " + smsResponse);

            // Get delivery reports. It may take a few seconds to show the above-sent message.
            var reportsResponse = sendSmsApi.getOutboundSmsMessageDeliveryReports().execute();
            log.info("Delivery reports: {}", reportsResponse.getResults());
            smsRepository.save(sms);
        } catch (ApiException e) {
            log.error("HTTP status code: {}", e.responseStatusCode());
            log.error("Response body: {}", e.rawResponseBody());
            throw new SmsException("Error on sending SMS");
        }
    }

    public void verifyPhoneNumberBySmsCode(VerificationRequest request) {
        try {
            UserDto userDto = userService.findUserByPhone(request.phoneNumber());
            Optional<SMS> sms = smsRepository.findByCode(request.code());

            if (sms.isEmpty() || !userDto.id().equals(sms.get().getUser().getId())) {
                throw new VerificationException("Code is not valid");
            }

            if (sms.get().getExpirationTime().isBefore(LocalDateTime.now())) {
                throw new VerificationException("Code is expired");
            }

            userService.save(
                    new UserDto(
                            userDto.id(),
                            userDto.phoneNumber(),
                            userDto.name(),
                            userDto.surname(),
                            userDto.dob(),
                            userDto.email(),
                            userDto.sex(),
                            true)
            );

//            TODO: sync user with other services

            log.info("{} is verified!", request.phoneNumber());
        } catch (NoSuchElementException e) {
            throw new VerificationException("User by phone number " + request.phoneNumber() + " not found.");
        }
    }

    private String createVerificationCode() {
        var random = new Random();
        int min = 100000;
        int max = 999999;
        int generatedCode = random.nextInt(max - min + 1) + min;
        return String.valueOf(generatedCode);
    }
}
