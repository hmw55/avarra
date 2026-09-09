package dev.hollandwesley.avarra.auth;

import dev.hollandwesley.avarra.user.domain.User;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        org.junit.jupiter.api.Assertions.assertNotEquals(
                originalSessionId,
                authenticatedSession.getId()
        );

        mockMvc.perform(get("/api/auth/me")
                        .session(authenticatedSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME));
    }
}