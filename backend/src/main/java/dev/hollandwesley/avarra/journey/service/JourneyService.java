package dev.hollandwesley.avarra.journey.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hollandwesley.avarra.journey.api.JourneySummaryResponse;
import dev.hollandwesley.avarra.journey.persistence.JourneyRepository;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

/**
 * Coordinates registered-user journey retrieval.
 *
 * <p>Authenticated usernames are resolved to persisted users before their
 * journeys are queried and mapped to API response models.
 */
@Service
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final UserRepository userRepository;

    public JourneyService(
            JourneyRepository journeyRepository,
            UserRepository userRepository) {
        this.journeyRepository = journeyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns journey summaries belonging to the registered user identified
     * by the authenticated username.
     *
     * @param username the authenticated user's username
     * @return the journeys belonging to the resolved registered user
     * @throws IllegalStateException if the authenticated user cannot be resolved
     */
    @Transactional(readOnly = true)
    public List<JourneySummaryResponse> getJourneysForUser(String username) {
        var user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user does not exist"));

        return journeyRepository.findByUserId(user.getId())
                .stream()
                .map(journey -> new JourneySummaryResponse(
                        journey.getId(),
                        journey.getName(),
                        journey.getCreatedAt(),
                        journey.getUpdatedAt()))
                .toList();
    }
}