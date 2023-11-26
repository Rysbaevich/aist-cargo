package kg.rysbaevich.userservice.model.dto;

import kg.rysbaevich.userservice.enums.Sex;
import kg.rysbaevich.userservice.model.User;

import java.time.LocalDate;

public record UserDto(
        Long id,
        String phoneNumber,
        String name,
        String surname,
        LocalDate dob,
        String email,
        Sex sex,
        boolean verified) {
    public UserDto(User user) {
        this(
                user.getId(),
                user.getPhoneNumber(),
                user.getName(),
                user.getSurname(),
                user.getDob(),
                user.getEmail(),
                user.getSex(),
                user.isVerified()
        );
    }

    public UserDto(String phoneNumber, boolean verified) {
        this(null, phoneNumber, null, null, null, null, null, verified);
    }
}
