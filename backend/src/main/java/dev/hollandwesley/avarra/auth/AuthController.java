package dev.hollandwesley.avarra.auth;

import dev.hollandwesley.avarra.user.RegisterUserRequest;
import dev.hollandwesley.avarra.user.RegisterUserResult;
import dev.hollandwesley.avarra.user.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes HTTP endpoints for Avarra account authentication and registration.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService registrationService;

    public AuthController(UserRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResult register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        return registrationService.register(request);
    }
}