//package mb.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import mb.config.jwt.JwtService;
//import mb.dto.request.SignInRequest;
//import mb.dto.request.SignUpRequest;
//import mb.dto.response.SignInResponse;
//import mb.dto.response.SimpleResponse;
//import mb.entities.User;
//import mb.enums.Role;
//import mb.exceptions.NotFoundException;
//import mb.repository.UserRepository;
//import mb.service.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.nio.file.AccessDeniedException;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//
//    @Override
//    public SignInResponse signIn(SignInRequest signInRequest) throws AccessDeniedException {
//        User user = userRepository.findByEmail(signInRequest.getLogin()).orElseThrow(()
//                -> new AccessDeniedException("Пользователь с электронной почтой " + signInRequest.getLogin() + " не найден!"));
//        boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
//        if (!matches) throw new NotFoundException("Неверный пароль");
//        return SignInResponse.builder()
//                .token(jwtService.createToken(user))
//                .id(user.getId())
//                .email(user.getEmail())
//                .role(user.getRole())
//                .httpStatus(HttpStatus.OK)
//                .message("Успешный вход")
//                .build();
//    }
//
//    @Override
//    public SimpleResponse signUp(SignUpRequest signUpRequest) {
//        if (userRepository.findByEmail(signUpRequest.email()).isPresent()) {
//            throw new IllegalArgumentException("Пользователь с электронной почтой " + signUpRequest.email() + " уже существует!");
//        }
//        User user = new User();
//        user.setFirstName(signUpRequest.firstName());
//        user.setLastName(signUpRequest.lastName());
//        user.setEmail(signUpRequest.email());
//        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
//        user.setPhoneNumber(signUpRequest.phoneNumber());
//        user.setRole(Role.USER);
//        userRepository.save(user);
//        return SimpleResponse.builder()
//                .httpStatus(HttpStatus.CREATED)
//                .message("Авторизация успешна!")
//                .build();
//    }
//    private User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            String email = authentication.getName(); // Получаем имя пользователя (например, email)
//            return userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//        }
//        throw new RuntimeException("User is not authenticated");
//    }
//}
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws NotFoundException {
        User user = userRepository.findByEmail(signInRequest.getLogin())
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", signInRequest.getLogin());
                    return new NotFoundException("Пользователь с электронной почтой " + signInRequest.getLogin() + " не найден!");
                });

        boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
        if (!matches) {
            logger.error("Invalid password attempt for user: {}", signInRequest.getLogin());
            throw new NotFoundException("Неверный пароль");
        }

        String token = jwtService.createToken(user);
        logger.info("User {} successfully signed in", signInRequest.getLogin());
        return SignInResponse.builder()
                .token(token)
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
            logger.error("User already exists with email: {}", signUpRequest.email());
            throw new IllegalArgumentException("Пользователь с электронной почтой " + signUpRequest.email() + " уже существует!");
        }

        User user = new User();
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setPhoneNumber(signUpRequest.phoneNumber());
        user.setRole(Role.USER);
        userRepository.save(user);
        logger.info("New user registered with email: {}", signUpRequest.email());

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Авторизация успешна!")
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));
        }
        throw new RuntimeException("User is not authenticated");
    }
}
