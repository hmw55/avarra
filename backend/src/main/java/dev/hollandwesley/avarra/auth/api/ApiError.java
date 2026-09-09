package dev.hollandwesley.avarra.auth.api;

/**
 * Represents a consistent error response returned by the Avarra API.
 */
public record ApiError(
        int status,
        String error,
        String message
) {
}