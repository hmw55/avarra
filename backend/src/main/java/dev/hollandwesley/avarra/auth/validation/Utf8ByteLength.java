package dev.hollandwesley.avarra.auth.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


/**
 * Validates that a value does not exceed a configured number of bytes when
 * encoded as UTF-8.
 * 
 * <p>This constraint is useful when an underlying system imposes a byte-based
 * limit rather that a character-based limit. In Avarra, it is used to enforce
 * BCrypt's 72-byte password input boundary so passwords are rejected rather 
 * than silently truncated.
 * 
 * <p>{@code null} values are considered valid. Required values should use an
 * additional constraint such as {@code @NotBlank}
 */
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Utf8ByteLengthValidator.class)
public @interface Utf8ByteLength {

    String message() default "must not exceed the allowed UTF-8 byte length";

    /**
     * Maximum permitted UTF-8 byte length
     */
    int max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}