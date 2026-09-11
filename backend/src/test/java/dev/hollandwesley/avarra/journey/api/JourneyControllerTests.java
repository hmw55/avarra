package dev.hollandwesley.avarra.journey.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import dev.hollandwesley.avarra.journey.service.JourneyService;

/**
 * Tests the Journey HTTP API contract for authenticated journey retrieval.
 */
@WebMvcTest(JourneyController.class)
class JourneyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JourneyService journeyService;

    @Test
    void returnsJourneysForAuthenticatedUser() throws Exception {
        UUID journeyId = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-09-11T15:00:00Z");
        Instant updatedAt = Instant.parse("2026-09-11T15:30:00Z");

        when(journeyService.getJourneysForUser("Mack_98"))
                .thenReturn(List.of(
                        new JourneySummaryResponse(
                                journeyId,
                                "Stonewake",
                                createdAt,
                                updatedAt)));

        // @WebMvcTest does not load the application's full SecurityConfig.
        // Supply the Authentication principal directly so the controller receives
        // the same username it would receive from an authenticated request.
        Authentication authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        "Mack_98",
                        null,
                        List.of());
        mockMvc.perform(get("/api/journeys")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(journeyId.toString()))
                .andExpect(jsonPath("$[0].name").value("Stonewake"))
                .andExpect(jsonPath("$[0].createdAt")
                        .value("2026-09-11T15:00:00Z"))
                .andExpect(jsonPath("$[0].updatedAt")
                        .value("2026-09-11T15:30:00Z"));
    }
}