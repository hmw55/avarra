package dev.hollandwesley.avarra.security;

import dev.hollandwesley.avarra.user.RegisterUserResult;
import dev.hollandwesley.avarra.user.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRegistrationService registrationService;

    @Test
    void registrationIsPubliclyAccessible() throws Exception {
        when(registrationService.register(any()))
                .thenReturn(new RegisterUserResult(
                        UUID.randomUUID(),
                        "SecurityTestUser_01",
                        "AVARRA-K7M4-P9TX-R2QW-8N3C"
                ));

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "SecurityTestUser_01",
                                  "password": "AvarraTestPassword123!",
                                  "email": "player@example.com"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void registrationRejectsRequestWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "SecurityTestUser_02",
                                  "password": "AvarraTestPassword123!",
                                  "email": "player@example.com"
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}