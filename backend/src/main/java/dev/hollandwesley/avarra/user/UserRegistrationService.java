package dev.hollandwesley.avarra.user;

import dev.hollandwesley.avarra.security.RecoveryCodeGenerator;
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