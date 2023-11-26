package kg.rysbaevich.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import kg.rysbaevich.userservice.enums.Sex;
import kg.rysbaevich.userservice.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;
    private String name;
    private String surname;
    private LocalDate dob;
    @Column(name = "email", unique = true)
    @Email
    private String email;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private boolean verified;

    public User(UserDto userDto) {
        this.setId(userDto.id());
        this.phoneNumber = userDto.phoneNumber();
        this.name = userDto.name();
        this.surname = userDto.surname();
        this.dob = userDto.dob();
        this.email = userDto.email();
        this.sex = userDto.sex();
    }
}
