package dev.hollandwesley.avarra.user;

import java.util.UUID;

/**
 * Represents the result of successfully registering an Avarra user.
 *
 * <p>The recovery code contains sensitive account-recovery material and should
 * only be returned to the user at registration time.
 */
public record RegisterUserResult(
        UUID userId,
        String username,
        String recoveryCode
) {
}