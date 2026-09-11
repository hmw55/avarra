package dev.hollandwesley.avarra.journey.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hollandwesley.avarra.journey.domain.Journey;

/**
 * Provides persistence operations for registered-user journeys.
 */
public interface JourneyRepository extends JpaRepository<Journey, UUID> {

    List<Journey> findByUserId(UUID userId);
}