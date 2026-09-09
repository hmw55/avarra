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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * Exposes HTTP endpoints for Avarra account authentication and registration.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
        UserRegistrationService registrationService,
        AuthenticationManager authenticationManager,
        SecurityContextRepository securityContextRepository
    ) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResult register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        return registrationService.register(request);
    }
    
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.username(),
                        request.password()
                )
        );

        SecurityContext securityContext =
                SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        securityContextRepository.saveContext(
                securityContext,
                httpRequest,
                httpResponse
        );

        return new LoginResponse(authentication.getName());
    }
}