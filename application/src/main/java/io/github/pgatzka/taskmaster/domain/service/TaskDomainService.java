package io.github.pgatzka.taskmaster.domain.service;

import io.github.pgatzka.taskmaster.domain.dto.TaskDTO;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import io.github.pgatzka.taskmaster.domain.exception.EntityNotFoundException;
import io.github.pgatzka.taskmaster.domain.exception.UniqueConflictException;
import io.github.pgatzka.taskmaster.domain.exception.VersionMismatchException;
import io.github.pgatzka.taskmaster.domain.mapper.TaskDomainMapper;
import io.github.pgatzka.taskmaster.domain.probe.TaskProbe;
import io.github.pgatzka.taskmaster.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private static final String TITLE_CONSTRAINT = "uk_task__title";

    private final TaskRepository taskRepository;

    private final TaskDomainMapper taskDomainMapper;

    @Transactional
    public @NonNull TaskDTO create(@NonNull TaskDTO dto) {
        if (taskRepository.existsByTitle(dto.title())) {
            throw titleConflict(dto.title(), null);
        }
        try {
            return taskDomainMapper.toDTO(taskRepository.saveAndFlush(taskDomainMapper.toEntity(dto)));
        } catch (DataIntegrityViolationException e) {
            throw isTitleConflict(e) ? titleConflict(dto.title(), e) : e;
        }
    }

    @Transactional(readOnly = true)
    public @NonNull TaskDTO findByKey(@NonNull UUID key) {
        return taskDomainMapper.toDTO(getByKey(key));
    }

    @Transactional(readOnly = true)
    public @NonNull Page<TaskDTO> findAll(@NonNull TaskProbe probe, @NonNull Pageable pageable) {
        return taskRepository.findAll(probe.toSpecification(), pageable).map(taskDomainMapper::toDTO);
    }

    @Transactional
    public @NonNull TaskDTO update(@NonNull UUID key, @NonNull Long version, @NonNull TaskDTO dto) {
        if (taskRepository.existsByTitleAndKeyNot(dto.title(), key)) {
            throw titleConflict(dto.title(), null);
        }
        TaskEntity taskEntity = getByKey(key);
        checkVersion(key, taskEntity.getVersion(), version);
        taskDomainMapper.update(dto, taskEntity);
        try {
            return taskDomainMapper.toDTO(taskRepository.saveAndFlush(taskEntity));
        } catch (ObjectOptimisticLockingFailureException e) {
            throw concurrentModification(key, e);
        } catch (DataIntegrityViolationException e) {
            throw isTitleConflict(e) ? titleConflict(dto.title(), e) : e;
        }
    }

    @Transactional
    public void delete(@NonNull UUID key, @NonNull Long version) {
        TaskEntity taskEntity = getByKey(key);
        checkVersion(key, taskEntity.getVersion(), version);
        try {
            taskRepository.delete(taskEntity);
            taskRepository.flush();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw concurrentModification(key, e);
        }
    }

    private void checkVersion(@NonNull UUID key, @Nullable Long expectedVersion, @Nullable Long providedVersion) {
        if (!Objects.equals(expectedVersion, providedVersion)) {
            throw new VersionMismatchException("task with key '%s' has version '%s', got '%s'".formatted(key, expectedVersion, providedVersion));
        }
    }

    private boolean isTitleConflict(DataIntegrityViolationException e) {
        return e.getCause() instanceof ConstraintViolationException cause && TITLE_CONSTRAINT.equalsIgnoreCase(cause.getConstraintName());
    }

    private UniqueConflictException titleConflict(String title, @Nullable Throwable cause) {
        return new UniqueConflictException("task with title '%s' already exists".formatted(title), cause);
    }

    private VersionMismatchException concurrentModification(UUID key, ObjectOptimisticLockingFailureException cause) {
        return new VersionMismatchException("task with key '%s' was modified concurrently".formatted(key), cause);
    }

    private TaskEntity getByKey(UUID key) {
        return taskRepository.findByKey(key).orElseThrow(() -> new EntityNotFoundException("task with key '%s' does not exist".formatted(key)));
    }

}
