package io.github.pgatzka.taskmaster.domain.entity;

import io.github.pgatzka.taskmaster.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.jspecify.annotations.NonNull;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskEntity extends AbstractEntity {

    @Column(name = "title", nullable = false, unique = true)
    private @NonNull String title;

    @Column(name = "done", nullable = false)
    private boolean done;

}