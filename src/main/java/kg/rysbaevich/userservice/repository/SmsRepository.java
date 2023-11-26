package kg.rysbaevich.userservice.repository;

import kg.rysbaevich.userservice.model.SMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsRepository extends JpaRepository<SMS, Long> {
    Optional<SMS> findByCode(String code);
}
