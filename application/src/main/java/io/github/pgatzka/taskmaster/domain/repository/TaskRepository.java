package io.github.pgatzka.taskmaster.domain.repository;

import io.github.pgatzka.taskmaster.domain.AbstractRepository;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends AbstractRepository<TaskEntity> {

    boolean existsByTitleIgnoreCase(String title);
}
