package kg.rysbaevich.aistcargo.service;

import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsTextualMessage;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SmsService {
    private static final String BASE_URL = "https://vvyn6r.api.infobip.com";
    private static final String API_KEY = "b0f65981a6f22d13d50849e0d7587e84-9a1faa73-265d-4f13-8508-6e6b80adc289";

    public void verifyPhoneNumberBySmsCode(String phoneNumber) {
        // Create the API client and the Send SMS API instances.
        var apiClient = ApiClient.forApiKey(ApiKey.from(API_KEY))
                .withBaseUrl(BaseUrl.from(BASE_URL))
                .build();
        var sendSmsApi = new SmsApi(apiClient);

        // Create a message to send.
        String verificationCode = createVerificationCode();

        var smsMessage = new SmsTextualMessage()
                .addDestinationsItem(new SmsDestination().to(phoneNumber))
                .text("Уважаемый клиент Ваш код верификации для приложения AistCargo: " + verificationCode);

        // Create a send message request.
        var smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(Collections.singletonList(smsMessage));

        try {
            // Send the message.
            var smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest).execute();
            System.out.println("Response body: " + smsResponse);

            // Get delivery reports. It may take a few seconds to show the above-sent message.
            var reportsResponse = sendSmsApi.getOutboundSmsMessageDeliveryReports().execute();
            System.out.println(reportsResponse.getResults());
        } catch (ApiException e) {
            System.out.println("HTTP status code: " + e.responseStatusCode());
            System.out.println("Response body: " + e.rawResponseBody());
        }
    }

    private String createVerificationCode() {
        return String.valueOf((int) (Math.random() * 1_000_000));
    }
}
