package io.github.pgatzka.taskmaster.domain.probe;

import io.github.pgatzka.taskmaster.domain.Range;
import io.github.pgatzka.taskmaster.domain.base.AbstractEntity_;
import io.github.pgatzka.taskmaster.domain.base.Probe;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity_;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public record TaskProbe(@Nullable Range<@Nullable Instant> createdAt, @Nullable Range<@Nullable Instant> updatedAt,
                        @Nullable String title, @Nullable Boolean done) implements Probe<TaskEntity> {

    @Override
    public @NonNull Specification<TaskEntity> toSpecification() {
        return Specification.allOf(between(AbstractEntity_.createdAt, createdAt), between(AbstractEntity_.updatedAt, updatedAt), likeIgnoreCase(TaskEntity_.title, title), equal(TaskEntity_.done, done));
    }

}
