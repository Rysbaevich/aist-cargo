package kg.rysbaevich.userservice.controller;

import kg.rysbaevich.userservice.model.request.VerificationRequest;
import kg.rysbaevich.userservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SmsService smsService;
//    private final UserService userService;

    @GetMapping("/sms")
    public ResponseEntity<?> sendCodeToPhoneNumber(@RequestParam String phoneNumber) {
        smsService.send(phoneNumber);
        return ResponseEntity.accepted().body("SMS code send");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerificationRequest verificationRequest) {
        smsService.verifyPhoneNumberBySmsCode(verificationRequest);
        return ResponseEntity.ok("Verification success");
    }

/*
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String phoneNumber) {
        userService.logout(phoneNumber);
        return ResponseEntity.ok("logged out " + phoneNumber);
    }
*/
}
