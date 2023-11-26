package kg.rysbaevich.userservice.model.request;

public record VerificationRequest(String phoneNumber, String code) {
}
