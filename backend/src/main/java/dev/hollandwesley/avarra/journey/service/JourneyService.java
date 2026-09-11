package dev.hollandwesley.avarra.journey.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hollandwesley.avarra.journey.api.JourneySummaryResponse;
import dev.hollandwesley.avarra.journey.persistence.JourneyRepository;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

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