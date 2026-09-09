package dev.hollandwesley.avarra.auth;

/**
 * Represents the safe account information returned after successful login.
 */
public record LoginResponse(
        String username
) {
}