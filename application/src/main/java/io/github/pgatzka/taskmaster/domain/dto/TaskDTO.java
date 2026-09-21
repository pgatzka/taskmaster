package io.github.pgatzka.taskmaster.domain.dto;

import io.github.pgatzka.taskmaster.domain.AbstractDTO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TaskDTO(@Nullable UUID id, @Nullable Instant createdAt, @Nullable Instant updatedAt,
                      @Nullable Integer version, @NonNull String title, boolean done) implements AbstractDTO {

    public TaskDTO {
        Objects.requireNonNull(title);
    }

    public static TaskDTO of(@NonNull AbstractDTO dto, @NonNull String title, boolean done) {
        return new TaskDTO(dto.id(), dto.createdAt(), dto.updatedAt(), dto.version(), title, done);
    }

    public static TaskDTO create(@NonNull String title, boolean done) {
        return new TaskDTO(null, null, null, null, title, done);
    }

}
