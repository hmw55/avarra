package dev.hollandwesley.avarra.user;

import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserPersistenceTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void persistsAndLoadsUser() {
        UUID id = UUID.randomUUID();

        User user = new User(
                id,
                "TestUser",
                "password-hash",
                null,
                "recovery-code-hash"
        );

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User loadedUser = entityManager.find(User.class, id);

        assertNotNull(loadedUser);
        assertEquals(id, loadedUser.getId());
        assertEquals("TestUser", loadedUser.getUsername());
        assertEquals("password-hash", loadedUser.getPasswordHash());
        assertNull(loadedUser.getEmail());
        assertEquals("recovery-code-hash", loadedUser.getRecoveryCodeHash());
        assertNotNull(loadedUser.getCreatedAt());
        assertNotNull(loadedUser.getUpdatedAt());
    }

    @Test
    void findsUserByUsernameIgnoreCase() {
        User user = new User(
            UUID.randomUUID(),
            "TestUser",
            "password-hash",
            null,
            "recovery-code-hash"
        );

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsernameIgnoreCase("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("TestUser", foundUser.get().getUsername());
    }

    @Test
    void detectsExistingUsernameIgnoringCase() {
        User user = new User(
            UUID.randomUUID(),
            "TestUser",
            "password-hash",
            null,
            "recovery-code-hash"
        );

        userRepository.save(user);

        boolean exists = userRepository.existsByUsernameIgnoreCase("TESTUSER");

        assertTrue(exists);
    }
}