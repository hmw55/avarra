package dev.hollandwesley.avarra.auth.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import dev.hollandwesley.avarra.auth.validation.NullOrNotBlank;
import dev.hollandwesley.avarra.auth.validation.Utf8ByteLength;

/**
 * Contains the account information required to register a new Avarra user.
 */
public record RegisterUserRequest(

        @NotBlank
        @Size(min = 3, max = 32)
        @Pattern(regexp = "^[A-Za-z0-9_]+$")
        String username,

        @NotBlank
        @Size(min = 12)
        @Utf8ByteLength(max = 72)
        String password,

        @NullOrNotBlank
        @Email
        @Size(max = 254)
        String email
) {
}