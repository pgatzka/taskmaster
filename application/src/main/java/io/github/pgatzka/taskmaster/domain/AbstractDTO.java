package io.github.pgatzka.taskmaster.domain;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public interface AbstractDTO {

    @Nullable UUID id();

    @Nullable Instant createdAt();

    @Nullable Instant updatedAt();

    @Nullable Integer version();
}
