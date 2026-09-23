package io.github.pgatzka.taskmaster.domain.base;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public interface AbstractDTO {

    @Nullable Long id();

    @NonNull UUID key();

    @Nullable Instant createdAt();

    @Nullable Instant updatedAt();

    @Nullable Long version();

}
