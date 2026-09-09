package dev.hollandwesley.avarra.auth.application;

import dev.hollandwesley.avarra.auth.api.RegisterUserRequest;
import dev.hollandwesley.avarra.auth.api.RegisterUserResult;
import dev.hollandwesley.avarra.auth.security.RecoveryCodeGenerator;
import dev.hollandwesley.avarra.user.domain.User;
import dev.hollandwesley.avarra.user.persistence.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Coordinates the creation of new Avarra user accounts.
 */
@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecoveryCodeGenerator recoveryCodeGenerator;

    public UserRegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RecoveryCodeGenerator recoveryCodeGenerator
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.recoveryCodeGenerator = recoveryCodeGenerator;
    }

    public RegisterUserResult register(RegisterUserRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        String passwordHash = passwordEncoder.encode(request.password());

        String recoveryCode = recoveryCodeGenerator.generate();
        String recoveryCodeHash = passwordEncoder.encode(recoveryCode);

        User user = new User(
            UUID.randomUUID(),
            request.username(),
            passwordHash,
            request.email(),
            recoveryCodeHash
        );

        User savedUser = userRepository.save(user);

        return new RegisterUserResult(
            savedUser.getId(),
            savedUser.getUsername(),
            recoveryCode
        );
    }
}