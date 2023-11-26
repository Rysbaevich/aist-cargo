package kg.rysbaevich.userservice.exceptions;

public class SmsException extends RuntimeException {
    public SmsException(String message) {
        super(message);
    }
}
