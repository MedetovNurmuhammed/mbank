package mb.api;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mb.dto.request.SignInRequest;
import mb.dto.request.SignUpRequest;
import mb.dto.response.SignInResponse;
import mb.dto.response.SimpleResponse;
import mb.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final UserService userService;

    @PostMapping("/signIn")
    @Operation(description = "Войти")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest) throws AccessDeniedException {
        return userService.signIn(signInRequest);
    }
    @PostMapping("/signUp")
    @Operation(description = "Регистрация")
    public SimpleResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return userService.signUp(signUpRequest);
    }
}
