package kg.rysbaevich.userservice.service;

import kg.rysbaevich.userservice.model.request.VerificationRequest;

public interface SmsService {

    void send(String phoneNumber);
    void verifyPhoneNumberBySmsCode(VerificationRequest verificationRequest);
}
