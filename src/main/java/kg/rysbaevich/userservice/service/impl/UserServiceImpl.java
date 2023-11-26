package kg.rysbaevich.userservice.service.impl;

import kg.rysbaevich.userservice.model.User;
import kg.rysbaevich.userservice.model.dto.UserDto;
import kg.rysbaevich.userservice.repository.UserRepository;
import kg.rysbaevich.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto findUserByPhone(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        return new UserDto(optionalUser.orElseThrow());
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = userRepository.save(new User(userDto));
        return new UserDto(user);
    }

    @Override
    public void logout(String phoneNumber) {

    }
}
