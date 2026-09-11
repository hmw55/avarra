package dev.hollandwesley.avarra.journey.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hollandwesley.avarra.journey.domain.Journey;
import dev.hollandwesley.avarra.journey.persistence.JourneyRepository;
import dev.hollandwesley.avarra.user.domain.User;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

@ExtendWith(MockitoExtension.class)
class JourneyServiceTests {

    @Mock
    private JourneyRepository journeyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JourneyService journeyService;

    @Test
    void returnsJourneysForAuthenticatedUser() {
        UUID userId = UUID.randomUUID();

        User user = new User(
                userId,
                "mack",
                "hashed-password",
                null,
                "hashed-recovery-code");

        Journey stonewakeJourney = new Journey(
                UUID.randomUUID(),
                user,
                "Stonewake");

        Journey valdyrJourney = new Journey(
                UUID.randomUUID(),
                user,
                "Valdyr");

        when(userRepository.findByUsernameIgnoreCase("mack"))
                .thenReturn(Optional.of(user));

        when(journeyRepository.findByUserId(userId))
                .thenReturn(List.of(stonewakeJourney, valdyrJourney));

        var result = journeyService.getJourneysForUser("mack");

        assertThat(result)
                .extracting(response -> response.name())
                .containsExactly("Stonewake", "Valdyr");
    }

    @Test
    void throwsWhenAuthenticatedUserDoesNotExist() {
        when(userRepository.findByUsernameIgnoreCase("missing-user"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> journeyService.getJourneysForUser("missing-user"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Authenticated user does not exist");
    }
}