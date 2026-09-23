package io.github.pgatzka.taskmaster.domain.repository;

import io.github.pgatzka.taskmaster.domain.base.AbstractRepository;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends AbstractRepository<TaskEntity> {

    boolean existsByTitle(String title);

    Optional<TaskEntity> findByKey(UUID key);

    boolean existsByTitleAndKeyNot(String title, UUID key);
}
