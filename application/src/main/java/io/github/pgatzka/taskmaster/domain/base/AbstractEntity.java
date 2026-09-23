package io.github.pgatzka.taskmaster.domain.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private @Nullable Long id;

    @Column(name = "key", nullable = false, updatable = false)
    private @NonNull UUID key = UUID.randomUUID();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private @Nullable Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private @Nullable Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
