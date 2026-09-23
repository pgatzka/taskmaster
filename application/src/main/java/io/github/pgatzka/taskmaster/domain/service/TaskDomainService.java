package io.github.pgatzka.taskmaster.domain.service;

import io.github.pgatzka.taskmaster.domain.probe.TaskProbe;
import io.github.pgatzka.taskmaster.domain.dto.TaskDTO;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import io.github.pgatzka.taskmaster.domain.mapper.TaskDomainMapper;
import io.github.pgatzka.taskmaster.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskRepository taskRepository;

    private final TaskDomainMapper taskDomainMapper;

    public @NonNull TaskDTO create(@NonNull TaskDTO taskDTO) {
        if (taskRepository.existsByTitle(taskDTO.title())) {
            throw new RuntimeException("Task with title '%s' already exists".formatted(taskDTO.title()));
        }
        return taskDomainMapper.toDTO(taskRepository.save(taskDomainMapper.toEntity(taskDTO)));
    }

    public @NonNull TaskDTO findByKey(@NonNull UUID key) {
        return taskDomainMapper.toDTO(getByKey(key));
    }

    public @NonNull Page<TaskDTO> findAll(@NonNull TaskProbe probe, @NonNull Pageable pageable) {
        return taskRepository.findAll(probe.toSpecification(), pageable).map(taskDomainMapper::toDTO);
    }

    public @NonNull TaskDTO update(@NonNull UUID key, @NonNull Long version, @NonNull TaskDTO dto) {
        TaskEntity taskEntity = getByKey(key);
        checkVersion(key, taskEntity.getVersion(), version);
        taskDomainMapper.update(dto, taskEntity);
        return taskDomainMapper.toDTO(taskRepository.saveAndFlush(taskEntity));
    }

    public void delete(@NonNull UUID key, @NonNull Long version) {
        TaskEntity taskEntity = getByKey(key);
        checkVersion(key, taskEntity.getVersion(), version);
        taskRepository.delete(taskEntity);
    }

    private void checkVersion(@NonNull UUID key, @Nullable Long actualVersion, @Nullable Long expectedVersion) {
        if (!Objects.equals(actualVersion, expectedVersion)) {
            throw new ObjectOptimisticLockingFailureException(TaskEntity.class, key);
        }
    }

    private TaskEntity getByKey(UUID key) {
        return taskRepository.findByKey(key).orElseThrow(() -> new RuntimeException("Task with key '%s' not found".formatted(key)));
    }

}
