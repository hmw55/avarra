package dev.hollandwesley.avarra.auth.security;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import dev.hollandwesley.avarra.user.domain.User;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies Avarra's server-side authentication session behavior using the
 * full Spring application context.
 *
 * <p>These integration tests confirm that successful login persists
 * authentication across requests, that session fixation protection changes
 * an existing session identifier during authentication, and that logout
 * invalidates authenticated sessions while remaining protected by CSRF.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthSessionIntegrationTests {

    private static final String USERNAME = "SessionTestUser";
    private static final String PASSWORD = "AvarraTestPassword123!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanUp() {
        userRepository.findByUsernameIgnoreCase(USERNAME)
                .ifPresent(userRepository::delete);
    }

    @Test
    void loginPersistsAuthenticationAcrossRequests() throws Exception {
        User user = new User(
                UUID.randomUUID(),
                USERNAME,
                passwordEncoder.encode(PASSWORD),
                null,
                passwordEncoder.encode("AVARRA-TEST-RECOVERY-CODE")
        );

        userRepository.save(user);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "sessiontestuser",
                                  "password": "AvarraTestPassword123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andReturn();

        MockHttpSession session =
                (MockHttpSession) loginResult.getRequest().getSession(false);

        mockMvc.perform(get("/api/auth/me")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME));
    }

    @Test
    void loginChangesExistingSessionId() throws Exception {
        User user = new User(
                UUID.randomUUID(),
                USERNAME,
                passwordEncoder.encode(PASSWORD),
                null,
                passwordEncoder.encode("AVARRA-TEST-RECOVERY-CODE")
        );

        userRepository.save(user);

        MockHttpSession session = new MockHttpSession();
        String originalSessionId = session.getId();

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "SessionTestUser",
                                  "password": "AvarraTestPassword123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession authenticatedSession =
                (MockHttpSession) loginResult.getRequest().getSession(false);

        // Successful authentication must rotate the existing session ID to protect
        // against session fixation while preserving the authenticated session.
        assertNotEquals(
                originalSessionId,
                authenticatedSession.getId()
        );

        mockMvc.perform(get("/api/auth/me")
                        .session(authenticatedSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME));
    }

    @Test
    void logoutInvalidatesAuthenticatedSession() throws Exception {
        User user = new User(
                UUID.randomUUID(),
                USERNAME,
                passwordEncoder.encode(PASSWORD),
                null,
                passwordEncoder.encode("AVARRA-TEST-RECOVERY-CODE")
        );

        userRepository.save(user);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "SessionTestUser",
                                  "password": "AvarraTestPassword123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session =
                (MockHttpSession) loginResult.getRequest().getSession(false);

        mockMvc.perform(post("/api/auth/logout")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        org.junit.jupiter.api.Assertions.assertTrue(session.isInvalid());

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutRejectsRequestWithoutCsrfToken() throws Exception {
        User user = new User(
                UUID.randomUUID(),
                USERNAME,
                passwordEncoder.encode(PASSWORD),
                null,
                passwordEncoder.encode("AVARRA-TEST-RECOVERY-CODE")
        );

        userRepository.save(user);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "SessionTestUser",
                                  "password": "AvarraTestPassword123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session =
                (MockHttpSession) loginResult.getRequest().getSession(false);

        mockMvc.perform(post("/api/auth/logout")
                        .session(session))
                .andExpect(status().isForbidden());
    }
}