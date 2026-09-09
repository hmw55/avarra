package dev.hollandwesley.avarra.user;

/**
 * Contains the account information required to register a new Avarra user.
 */
public record RegisterUserRequest(
        String username,
        String password,
        String email
) {
}