package dev.hollandwesley.avarra.auth;

/**
 * Represents a consistent error response returned by the Avarra API.
 */
public record ApiError(
        int status,
        String error,
        String message
) {
}