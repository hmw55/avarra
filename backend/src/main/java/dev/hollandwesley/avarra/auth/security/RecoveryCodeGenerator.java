package dev.hollandwesley.avarra.auth.security;

import java.security.SecureRandom;

/**
 * Generates cryptographically secure recovery codes for Avarra accounts.
 */
public class RecoveryCodeGenerator {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int GROUP_COUNT = 4;
    private static final int GROUP_LENGTH = 4;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generate() {
        StringBuilder recoveryCode = new StringBuilder("AVARRA");

        for (int group = 0; group < GROUP_COUNT; group++) {
            recoveryCode.append('-');

            for (int character = 0; character < GROUP_LENGTH; character++) {
                int index = secureRandom.nextInt(ALPHABET.length());
                recoveryCode.append(ALPHABET.charAt(index));
            }
        }

        return recoveryCode.toString();
    }
}