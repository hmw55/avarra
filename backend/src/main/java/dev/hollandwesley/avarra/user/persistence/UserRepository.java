package dev.hollandwesley.avarra.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hollandwesley.avarra.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);
}