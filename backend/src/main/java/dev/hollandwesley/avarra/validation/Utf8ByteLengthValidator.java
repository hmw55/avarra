package dev.hollandwesley.avarra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;

/**
 * Validates that a string does not exceed a configured UTF-8 byte length.
 */
public class Utf8ByteLengthValidator
        implements ConstraintValidator<Utf8ByteLength, String> {

    private int max;

    @Override
    public void initialize(Utf8ByteLength constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        if (value == null) {
            return true;
        }

        return value.getBytes(StandardCharsets.UTF_8).length <= max;
    }
}