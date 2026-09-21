package io.github.pgatzka.taskmaster.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private @Nullable UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private @Nullable Instant createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    private @Nullable Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private @Nullable Integer version;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false;
        return id != null && id.equals(((AbstractEntity) other).id);
    }

    @Override
    public int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
