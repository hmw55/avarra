package dev.hollandwesley.avarra.auth.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordConfigTests {

    private final PasswordEncoder passwordEncoder =
            new PasswordConfig().passwordEncoder();

    @Test
    void hashesAndVerifiesPassword() {
        String rawPassword = "AvarraTestPassword123!";

        String passwordHash = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, passwordHash);
        assertTrue(passwordEncoder.matches(rawPassword, passwordHash));
        assertFalse(passwordEncoder.matches("wrong-password", passwordHash));
    }
}