package dev.hollandwesley.avarra.journey.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import dev.hollandwesley.avarra.journey.domain.Journey;
import dev.hollandwesley.avarra.user.domain.User;
import dev.hollandwesley.avarra.user.persistence.UserRepository;


/**
 * Tests Journey persistence and registered-user ownership queries
 * against the configured PostgreSQL database.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class JourneyPersistenceTests {

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void persistsAndLoadsJourney() {
        User user = new User(
                UUID.randomUUID(),
                "jt-" + UUID.randomUUID().toString().substring(0, 8),
                "hashed-password",
                null,
                "hashed-recovery-code");

        userRepository.saveAndFlush(user);

        Journey journey = new Journey(
                UUID.randomUUID(),
                user,
                "Stonewake");

        journeyRepository.saveAndFlush(journey);

        Optional<Journey> found = journeyRepository.findById(journey.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(journey.getId());
        assertThat(found.get().getName()).isEqualTo("Stonewake");
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findsJourneysByUserId() {
        User firstUser = new User(
                UUID.randomUUID(),
                "jt-" + UUID.randomUUID().toString().substring(0, 8),
                "hashed-password",
                null,
                "hashed-recovery-code");

        User secondUser = new User(
                UUID.randomUUID(),
                "jt-" + UUID.randomUUID().toString().substring(0, 8),
                "hashed-password",
                null,
                "hashed-recovery-code");

        userRepository.saveAndFlush(firstUser);
        userRepository.saveAndFlush(secondUser);

        Journey stonewakeJourney = new Journey(
                UUID.randomUUID(),
                firstUser,
                "Stonewake");

        Journey valdyrJourney = new Journey(
                UUID.randomUUID(),
                firstUser,
                "Valdyr");

        Journey otherUserJourney = new Journey(
                UUID.randomUUID(),
                secondUser,
                "Asterwyn");

        journeyRepository.save(stonewakeJourney);
        journeyRepository.save(valdyrJourney);
        journeyRepository.saveAndFlush(otherUserJourney);

        var journeys = journeyRepository.findByUserId(firstUser.getId());

        assertThat(journeys)
                .extracting(Journey::getName)
                .containsExactlyInAnyOrder("Stonewake", "Valdyr");
    }
}