package io.github.pgatzka.taskmaster.domain.dto;

import io.github.pgatzka.taskmaster.domain.base.AbstractDTO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public record TaskDTO(@Nullable Long id, @NonNull UUID key, @Nullable Instant createdAt, @Nullable Instant updatedAt,
                      @Nullable Long version, @NonNull String title, boolean done) implements AbstractDTO {

}
