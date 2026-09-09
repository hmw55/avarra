package dev.hollandwesley.avarra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that a string is either absent or contains non-whitespace
 * characters.
 */
public class NullOrNotBlankValidator
        implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        return value == null || !value.isBlank();
    }
}