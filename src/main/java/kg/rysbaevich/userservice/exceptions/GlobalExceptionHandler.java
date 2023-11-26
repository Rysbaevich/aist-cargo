package kg.rysbaevich.userservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SmsException.class, NotFoundException.class})
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({VerificationException.class})
    public ResponseEntity<?> handleVerificationException(Exception ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(ex.getMessage());
    }
}
