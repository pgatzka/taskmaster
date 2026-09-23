package io.github.pgatzka.taskmaster.domain.base;

import io.github.pgatzka.taskmaster.domain.Range;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public interface Probe<T extends AbstractEntity> {

    @Nullable Long id();

    @Nullable UUID key();

    @Nullable Range<@Nullable Instant> createdAt();

    @Nullable Range<@Nullable Instant> updatedAt();

    @Nullable Long version();

    @NonNull Specification<T> toSpecification();

}
