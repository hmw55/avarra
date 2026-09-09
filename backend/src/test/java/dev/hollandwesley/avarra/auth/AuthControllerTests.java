package dev.hollandwesley.avarra.auth;

import dev.hollandwesley.avarra.user.RegisterUserResult;
import dev.hollandwesley.avarra.user.UserRegistrationService;
import dev.hollandwesley.avarra.user.UsernameAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.security.core.context.SecurityContext;

import static org.mockito.Mockito.verify;

@WebMvcTest(AuthController.class)
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRegistrationService registrationService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private SecurityContextRepository securityContextRepository;

    @Test
    void registersUserAndReturnsCreated() throws Exception {
        UUID userId = UUID.randomUUID();

        when(registrationService.register(any()))
                .thenReturn(new RegisterUserResult(
                        userId,
                        "Mack_98",
                        "AVARRA-K7M4-P9TX-R2QW-8N3C"
                ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "Mack_98",
                                  "password": "AvarraTestPassword123!",
                                  "email": "player@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("Mack_98"))
                .andExpect(jsonPath("$.recoveryCode")
                        .value("AVARRA-K7M4-P9TX-R2QW-8N3C"));
    }

    @Test
    void rejectsInvalidRegistrationRequest() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("""
                                {
                                "username": "ab",
                                "password": "short",
                                "email": "not-an-email"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Registration request is invalid"));
    }

    @Test
    void returnsConflictWhenUsernameAlreadyExists() throws Exception {
        when(registrationService.register(any()))
                .thenThrow(new UsernameAlreadyExistsException("Mack_98"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("""
                                {
                                "username": "Mack_98",
                                "password": "AvarraTestPassword123!",
                                "email": "player@example.com"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Username is already in use"));
    }

    @Test
    void logsInUserAndReturnsUsername() throws Exception {
        Authentication authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        "Mack_98",
                        null,
                        java.util.List.of()
                );

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {
                                "username": "mack_98",
                                "password": "AvarraTestPassword123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Mack_98"));

        verify(securityContextRepository).saveContext(
            any(SecurityContext.class), 
            any(), 
            any()
        );
    }

    @Test
    void rejectsInvalidLoginCredentials() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {
                                "username": "Mack_98",
                                "password": "WrongPassword123!"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid username or password"));
    }
}