package dev.hollandwesley.avarra.auth.security;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dev.hollandwesley.avarra.user.domain.User;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies the Spring Security user-details adapter for registered Avarra users.
 * 
 * <p>These tests confirm case-insensitive account lookup, preservation of the 
 * user's canonical username, and rejection of unknown usernames.
 */
class AvarraUserDetailsServiceTests {

    @Test
    void loadsExistingUserIgnoringUsernameCase() {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                UUID.randomUUID(),
                "Mack_98",
                "$2a$10$examplePasswordHash",
                "player@example.com",
                "$2a$10$exampleRecoveryCodeHash"
        );

        when(userRepository.findByUsernameIgnoreCase("mack_98"))
                .thenReturn(Optional.of(user));

        AvarraUserDetailsService userDetailsService =
                new AvarraUserDetailsService(userRepository);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername("mack_98");

        assertEquals("Mack_98", userDetails.getUsername());
        assertEquals(
                "$2a$10$examplePasswordHash",
                userDetails.getPassword()
        );
    }

    @Test
    void rejectsUnknownUsername() {
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findByUsernameIgnoreCase("UnknownUser"))
                .thenReturn(Optional.empty());

        AvarraUserDetailsService userDetailsService =
                new AvarraUserDetailsService(userRepository);

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("UnknownUser")
        );
    }
}