package kg.rysbaevich.userservice.service;

import kg.rysbaevich.userservice.model.dto.UserDto;

public interface UserService {
    UserDto findUserByPhone(String phoneNumber);
    UserDto save(UserDto userDto);
    void logout(String phoneNumber);
}
