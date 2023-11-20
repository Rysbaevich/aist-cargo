package kg.rysbaevich.aistcargo.controller;

import kg.rysbaevich.aistcargo.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SmsService smsService;

    public AuthController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String phoneNumber) {
        return ResponseEntity.ok("logged in " + phoneNumber);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String phoneNumber) {
        smsService.verifyPhoneNumberBySmsCode(phoneNumber);
        return ResponseEntity.status(201).body("SMS code send");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String phoneNumber) {
        return ResponseEntity.ok("logged out " + phoneNumber);
    }
}
