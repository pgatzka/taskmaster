package io.github.pgatzka.taskmaster.domain.base;

import io.github.pgatzka.taskmaster.domain.Range;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.metamodel.SingularAttribute;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Locale;

public interface Probe<T extends AbstractEntity> {

    @Nullable Range<@Nullable Instant> createdAt();

    @Nullable Range<@Nullable Instant> updatedAt();

    @NonNull Specification<T> toSpecification();

    default <V> @NonNull Specification<T> equal(@NonNull SingularAttribute<? super T, V> attribute, @Nullable V value) {
        return value == null ? Specification.unrestricted() : (root, _, builder) -> builder.equal(root.get(attribute), value);
    }

    default @NonNull Specification<T> likeIgnoreCase(@NonNull SingularAttribute<? super T, String> attribute, @Nullable String value) {
        if (!StringUtils.hasText(value)) return Specification.unrestricted();

        String string = value.trim().toLowerCase(Locale.ROOT);

        return !StringUtils.hasText(string) ? Specification.unrestricted() : (root, _, builder) -> {
            String pattern = "%" + string.replace("\\", "\\\\").replace("_", "\\_").replace("%", "\\%") + "%";
            return builder.like(builder.lower(root.get(attribute)), pattern, '\\');
        };
    }

    default @NonNull Specification<T> between(@NonNull SingularAttribute<? super T, Instant> attribute, @Nullable Range<@Nullable Instant> range) {
        return range == null || (range.min() == null && range.max() == null) ? Specification.unrestricted() : (root, _, builder) -> {
            Path<Instant> path = root.get(attribute);
            if (range.min() == null) {
                return builder.lessThanOrEqualTo(path, range.max());
            }
            if (range.max() == null) {
                return builder.greaterThanOrEqualTo(path, range.min());
            }
            return builder.between(path, range.min(), range.max());
        };
    }


}
