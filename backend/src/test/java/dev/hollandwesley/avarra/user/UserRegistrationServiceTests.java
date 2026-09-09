package dev.hollandwesley.avarra.user;

import dev.hollandwesley.avarra.auth.api.RegisterUserRequest;
import dev.hollandwesley.avarra.auth.api.RegisterUserResult;
import dev.hollandwesley.avarra.auth.application.UserRegistrationService;
import dev.hollandwesley.avarra.auth.application.UsernameAlreadyExistsException;
import dev.hollandwesley.avarra.auth.security.RecoveryCodeGenerator;
import dev.hollandwesley.avarra.user.domain.User;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserRegistrationServiceTests {

    @Test
    void rejectsUsernameThatAlreadyExistsIgnoringCase() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RecoveryCodeGenerator recoveryCodeGenerator = new RecoveryCodeGenerator();

        UserRegistrationService registrationService = new UserRegistrationService(
                userRepository,
                passwordEncoder,
                recoveryCodeGenerator
        );

        when(userRepository.existsByUsernameIgnoreCase("testuser"))
                .thenReturn(true);

        RegisterUserRequest request = new RegisterUserRequest(
                "testuser",
                "AvarraTestPassword123!",
                null
        );

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> registrationService.register(request)
        );
    }

    @Test
    void registersUserWithHashedCredentials() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RecoveryCodeGenerator recoveryCodeGenerator = new RecoveryCodeGenerator();

        UserRegistrationService registrationService = new UserRegistrationService(
                userRepository,
                passwordEncoder,
                recoveryCodeGenerator
        );

        RegisterUserRequest request = new RegisterUserRequest(
                "NewUser",
                "AvarraTestPassword123!",
                "newuser@example.com"
        );

        when(userRepository.existsByUsernameIgnoreCase("NewUser"))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegisterUserResult result = registrationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("NewUser", savedUser.getUsername());
        assertEquals("newuser@example.com", savedUser.getEmail());

        assertNotEquals("AvarraTestPassword123!", savedUser.getPasswordHash());
        assertTrue(passwordEncoder.matches(
                "AvarraTestPassword123!",
                savedUser.getPasswordHash()
        ));

        assertNotEquals(result.recoveryCode(), savedUser.getRecoveryCodeHash());
        assertTrue(passwordEncoder.matches(
                result.recoveryCode(),
                savedUser.getRecoveryCodeHash()
        ));

        assertEquals(savedUser.getId(), result.userId());
        assertEquals("NewUser", result.username());
    }
}