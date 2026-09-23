package io.github.pgatzka.taskmaster.domain.probe;

import io.github.pgatzka.taskmaster.domain.Range;
import io.github.pgatzka.taskmaster.domain.base.Probe;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public record TaskProbe(
        @Nullable Long id,
        @Nullable UUID key,
        @Nullable Range<@Nullable Instant> createdAt,
        @Nullable Range<@Nullable Instant> updatedAt,
        @Nullable Long version,
        @Nullable String title,
        @Nullable Boolean done
) implements Probe<TaskEntity> {

    @Override
    public @NonNull Specification<TaskEntity> toSpecification() {
        return Specification.unrestricted();
    }

}
