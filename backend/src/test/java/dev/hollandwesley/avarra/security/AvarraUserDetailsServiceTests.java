package dev.hollandwesley.avarra.security;

import dev.hollandwesley.avarra.user.User;
import dev.hollandwesley.avarra.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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