package dev.hollandwesley.avarra.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.hollandwesley.avarra.auth.application.UserRegistrationService;


/**
 * Exposes HTTP endpoints for Avarra registration and authentication.
 * 
 * <p>Successful login establishes server-side session authentication through
 * Spring Security. Authentication state is persisted in the HTTP session rather
 * than returned to the client as a bearer token.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;

    public AuthController(
            UserRegistrationService registrationService,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            SessionAuthenticationStrategy sessionAuthenticationStrategy
    ) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResult register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        return registrationService.register(request);
    }
    
    /**
     * Authenticates a registered user and establishes an authenticated HTTP session.
     * 
     * <p>The session authentication strategy is applied before the authenticated
     * security context is persisted so that Spring Security's session fixation
     * protection is honored for the new login.
     * 
     * @param request submitted username and password
     * @param httpRequest current HTTP request used to establish the session
     * @param httpResponse current HTTP response associated with the session
     * @return the authenticated user's canonical username
     */
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

        sessionAuthenticationStrategy.onAuthentication(
            authentication, 
            httpRequest, 
            httpResponse
        );

        // Persist authentication explicitly because this JSON login endpoint does not
        // use Spring Security's form-login filter to establish the security context.
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

    @GetMapping("/me")
    public LoginResponse currentUser(Authentication authentication) {
        return new LoginResponse(authentication.getName());
    }
}