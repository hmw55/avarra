package dev.hollandwesley.avarra.auth.api;

/**
 * Represents safe authenticated-account information returned by the Avarra API
 */
public record LoginResponse(
        String username
) {
}