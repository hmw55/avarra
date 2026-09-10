package dev.hollandwesley.avarra.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hollandwesley.avarra.user.domain.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides persistence operations for registered Avarra user accounts. 
 * 
 * <p>Username lookups and existence checks are case-insensitive to match
 * Avarra's username uniqueness rules.
 */

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);
}