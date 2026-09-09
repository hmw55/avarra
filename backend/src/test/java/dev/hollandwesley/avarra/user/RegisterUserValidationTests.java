package dev.hollandwesley.avarra.user;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterUserRequestValidationTests {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void acceptsValidUsername() {
        RegisterUserRequest request = new RegisterUserRequest(
                "Mack_98",
                "AvarraTestPassword123!",
                null
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsUsernameThatIsTooShort() {
        RegisterUserRequest request = new RegisterUserRequest(
                "ab",
                "AvarraTestPassword123!",
                null
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsUsernameWithInvalidCharacters() {
        RegisterUserRequest request = new RegisterUserRequest(
                "Mack-Wesley",
                "AvarraTestPassword123!",
                null
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptsPasswordAtSeventyTwoUtf8Bytes() {
        RegisterUserRequest request = new RegisterUserRequest(
                "ValidUser",
                "a".repeat(72),
                null
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsPasswordOverSeventyTwoUtf8Bytes() {
        RegisterUserRequest request = new RegisterUserRequest(
                "ValidUser",
                "a".repeat(73),
                null
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsPasswordUnderSeventyTwoCharactersButOverSeventyTwoUtf8Bytes() {
        RegisterUserRequest request = new RegisterUserRequest(
                "ValidUser",
                "é".repeat(37),
                null
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptsValidEmail() {
        RegisterUserRequest request = new RegisterUserRequest(
                "ValidUser",
                "AvarraTestPassword123!",
                "player@example.com"
        );

        assertTrue(validator.validate(request).isEmpty());
    }
    @Test

    void rejectsInvalidEmail() {
        RegisterUserRequest request = new RegisterUserRequest(
                "ValidUser",
                "AvarraTestPassword123!",
                "not-an-email"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsBlankEmail() {
        RegisterUserRequest request = new RegisterUserRequest(
                "ValidUser",
                "AvarraTestPassword123!",
                "   "
        );

        assertFalse(validator.validate(request).isEmpty());
    }
}