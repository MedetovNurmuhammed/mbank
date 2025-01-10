package mb.service.impl;

import lombok.RequiredArgsConstructor;
import mb.config.jwt.JwtService;
import mb.dto.request.SignInRequest;
import mb.dto.request.SignUpRequest;
import mb.dto.response.SignInResponse;
import mb.dto.response.SimpleResponse;
import mb.entities.User;
import mb.enums.Role;
import mb.exceptions.NotFoundException;
import mb.repository.UserRepository;
import mb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws AccessDeniedException {
        User user = userRepository.findByEmail(signInRequest.getLogin()).orElseThrow(()
                -> new AccessDeniedException("Пользователь с электронной почтой " + signInRequest.getLogin() + " не найден!"));
        boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
        if (!matches) throw new NotFoundException("Неверный пароль");
        return SignInResponse.builder()
                .token(jwtService.createToken(user))
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .httpStatus(HttpStatus.OK)
                .message("Успешный вход")
                .build();
    }

    @Override
    public SimpleResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с электронной почтой " + signUpRequest.email() + " уже существует!");
        }
        User user = new User();
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setPhoneNumber(signUpRequest.phoneNumber());
        user.setRole(Role.User);
        userRepository.save(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Авторизация успешна!")
                .build();
    }
}
