package io.github.pgatzka.taskmaster.domain.service;

import io.github.pgatzka.taskmaster.domain.dto.TaskDTO;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import io.github.pgatzka.taskmaster.domain.exception.TaskNotFoundException;
import io.github.pgatzka.taskmaster.domain.exception.TaskTitleAlreadyExistsException;
import io.github.pgatzka.taskmaster.domain.mapper.TaskMapper;
import io.github.pgatzka.taskmaster.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    @Transactional
    public @NonNull TaskDTO create(@NonNull TaskDTO taskDTO) {
        if (taskRepository.existsByTitleIgnoreCase(taskDTO.title())) {
            throw new TaskTitleAlreadyExistsException(taskDTO.title());
        }
        TaskEntity entity = taskRepository.save(taskMapper.toEntity(taskDTO));
        log.info("created task: {}", entity.getId());
        return taskMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public @NonNull TaskDTO findById(@NonNull UUID id) {
        log.debug("loading task: {}", id);
        return taskMapper.toDTO(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public @NonNull List<TaskDTO> findAll() {
        List<TaskDTO> tasks = taskRepository.findAll().stream().map(taskMapper::toDTO).toList();
        log.debug("Found {} tasks", tasks.size());
        return tasks;
    }

    @Transactional
    public @NonNull TaskDTO update(@NonNull TaskDTO taskDTO) {
        Objects.requireNonNull(taskDTO.id());
        TaskEntity entity = findEntityById(taskDTO.id());
        checkVersion(entity, taskDTO.version());
        taskMapper.update(taskDTO, entity);
        TaskEntity updatedEntity = taskRepository.saveAndFlush(entity);
        log.info("updated task {} to version {}", updatedEntity.getId(), updatedEntity.getVersion());
        return taskMapper.toDTO(updatedEntity);
    }

    @Transactional
    public void delete(@NonNull TaskDTO taskDTO) {
        Objects.requireNonNull(taskDTO.id());
        TaskEntity taskEntity = findEntityById(taskDTO.id());
        checkVersion(taskEntity, taskDTO.version());
        taskRepository.delete(taskEntity);
        log.info("deleted task: {}", taskEntity.getId());
    }

    private void checkVersion(@NonNull TaskEntity entity, @Nullable Integer version) {
        if (!Objects.equals(entity.getVersion(), version)) {
            Objects.requireNonNull(entity.getId());
            log.debug("Version conflict on task {}: expected {}, got {}", entity.getId(), entity.getVersion(), version);
            throw new ObjectOptimisticLockingFailureException(TaskEntity.class, entity.getId());
        }
    }

    private @NonNull TaskEntity findEntityById(@NonNull UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }


}
