package dev.hollandwesley.avarra.user.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
/**
 * Represents a registered Avarra user account.
 *
 * <p>A user owns authentication credentials and account-level identity.
 * Game characters and game state are modeled separately from the user account.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "username", nullable = false, length = 32)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "email", length = 254)
    private String email;

    @Column(name = "recovery_code_hash", nullable = false, length = 255)
    private String recoveryCodeHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected User() {
        // Required by JPA.
    }

    /**
     * Creates a registered user from already-processed account data.
     * 
     * <p>Password and recovery-code values supplied here must already be hashed;
     * raw credentials must not be stored in the user entity.
     */
    public User(
        UUID id,
        String username,
        String passwordHash,
        String email,
        String recoveryCodeHash
    ) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.recoveryCodeHash = recoveryCodeHash;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRecoveryCodeHash() {
        return recoveryCodeHash;
    }

    @PrePersist
    private void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = Instant.now();
    }
}