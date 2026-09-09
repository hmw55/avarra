package dev.hollandwesley.avarra.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Contains the credentials required to authenticate an Avarra user.
 */
public record LoginRequest(

        @NotBlank
        String username,

        @NotBlank
        String password

) {
}