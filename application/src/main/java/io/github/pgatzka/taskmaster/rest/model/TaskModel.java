package io.github.pgatzka.taskmaster.rest.model;

import java.time.Instant;
import java.util.UUID;

public record TaskModel(
        UUID key,
        Instant createdAt,
        Instant updatedAt,
        Long version,
        String title,
        boolean done
) {
}
