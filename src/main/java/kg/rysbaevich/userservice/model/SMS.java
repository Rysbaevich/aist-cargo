package kg.rysbaevich.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMS extends BaseEntity {
    private String code;
    private LocalDateTime expirationTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
