package dev.hollandwesley.avarra.journey.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import dev.hollandwesley.avarra.journey.service.JourneyService;

/**
 * Provides authenticated access to a user's journeys.
 */
@RestController
@RequestMapping("/api/journeys")
public class JourneyController {

    private final JourneyService journeyService;

    public JourneyController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }

    @GetMapping
    public List<JourneySummaryResponse> getJourneys(Authentication authentication) {
        return journeyService.getJourneysForUser(authentication.getName());
    }
}