
package mb.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mb.dto.request.SignInRequest;
import mb.dto.request.SignUpRequest;
import mb.dto.response.SignInResponse;
import mb.dto.response.SimpleResponse;
import mb.dto.error.ErrorResponse;
import mb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthApi.class);

    @PostMapping("/signIn")
    @Operation(description = "Войти")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        try {
            logger.info("Received signIn request for user: {}", signInRequest.getLogin());
            SignInResponse signInResponse = userService.signIn(signInRequest);
            return ResponseEntity.ok(signInResponse);
        } catch (AccessDeniedException e) {
            logger.error("Access Denied for signIn", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Access Denied", e.getMessage()).toXml());
        } catch (Exception e) {
            logger.error("Error during signIn", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("SignIn Error", e.getMessage()).toXml());
        }
    }

    @PostMapping("/signUp")
    @Operation(description = "Регистрация")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        try {
            logger.info("Received signUp request for user: {}", signUpRequest.email());
            SimpleResponse simpleResponse = userService.signUp(signUpRequest);
            return ResponseEntity.ok(simpleResponse);
        } catch (Exception e) {
            logger.error("Error during signUp", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("SignUp Error", e.getMessage()).toXml());
        }
    }
}
