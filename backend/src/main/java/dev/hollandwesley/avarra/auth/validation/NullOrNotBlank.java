package dev.hollandwesley.avarra.auth.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


/**
 * Validates that a value is either {@code null} or contains at least one
 * non-whitespace character.
 * 
 * <p>This constraint is intended for optional values where omission is valid,
 * but supplying a blank value is not. Required fields should use an
 * appropriate required-value constraint such as {@code @NotBlank}
 */
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {

    String message() default "must be omitted or contain a value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}