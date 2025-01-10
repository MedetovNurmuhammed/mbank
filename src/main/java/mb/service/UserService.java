package mb.service;

import jakarta.validation.Valid;
import mb.dto.request.SignInRequest;
import mb.dto.request.SignUpRequest;
import mb.dto.response.SignInResponse;
import mb.dto.response.SimpleResponse;

import java.nio.file.AccessDeniedException;

public interface UserService {
    SignInResponse signIn(@Valid SignInRequest signInRequest) throws AccessDeniedException;

    SimpleResponse signUp(@Valid SignUpRequest signUpRequest);
}
