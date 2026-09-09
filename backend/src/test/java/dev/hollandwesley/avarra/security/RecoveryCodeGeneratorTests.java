package dev.hollandwesley.avarra.security;

import org.junit.jupiter.api.Test;

import dev.hollandwesley.avarra.auth.security.RecoveryCodeGenerator;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecoveryCodeGeneratorTests {

    private final RecoveryCodeGenerator recoveryCodeGenerator =
            new RecoveryCodeGenerator();

    @Test
    void generatesRecoveryCodeInExpectedFormat() {
        String recoveryCode = recoveryCodeGenerator.generate();

        assertTrue(
                recoveryCode.matches(
                        "^AVARRA-[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{4}" +
                        "-[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{4}" +
                        "-[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{4}" +
                        "-[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{4}$"
                )
        );
    }

    @Test
    void generatesDifferentRecoveryCodes() {
        String firstRecoveryCode = recoveryCodeGenerator.generate();
        String secondRecoveryCode = recoveryCodeGenerator.generate();

        assertNotEquals(firstRecoveryCode, secondRecoveryCode);
    }
}