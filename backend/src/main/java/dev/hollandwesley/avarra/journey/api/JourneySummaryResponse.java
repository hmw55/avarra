package dev.hollandwesley.avarra.journey.api;

import java.time.Instant;
import java.util.UUID;

/**
 * Minimal API representation of a registered user's journey.
 */
public record JourneySummaryResponse(
        UUID id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}