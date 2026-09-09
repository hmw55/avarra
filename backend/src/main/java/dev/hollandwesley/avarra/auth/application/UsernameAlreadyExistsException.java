package dev.hollandwesley.avarra.auth.application;

/**
 * Thrown when registration attempts to use a username that is already taken.
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("Username is already in use: " + username);
    }
}