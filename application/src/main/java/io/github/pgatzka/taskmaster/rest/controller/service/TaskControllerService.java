package io.github.pgatzka.taskmaster.rest.controller.service;

import io.github.pgatzka.taskmaster.domain.service.TaskDomainService;
import io.github.pgatzka.taskmaster.rest.mapper.TaskRestMapper;
import io.github.pgatzka.taskmaster.rest.model.TaskModel;
import io.github.pgatzka.taskmaster.rest.request.CreateTaskRequest;
import io.github.pgatzka.taskmaster.rest.request.TaskFilterRequest;
import io.github.pgatzka.taskmaster.rest.request.UpdateTaskRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskControllerService {

    private final TaskDomainService domainService;

    private final TaskRestMapper taskRestMapper;

    public @NonNull TaskModel create(@NotNull CreateTaskRequest request) {
        return taskRestMapper.toModel(domainService.create(taskRestMapper.toDTO(request)));
    }

    public @NonNull TaskModel get(@NonNull UUID key) {
        return taskRestMapper.toModel(domainService.findByKey(key));
    }

    public @NonNull PagedModel<TaskModel> get(@NonNull TaskFilterRequest filter, @NonNull Pageable pageable) {
        return new PagedModel<>(domainService.findAll(taskRestMapper.toProbe(filter), pageable).map(taskRestMapper::toModel));
    }

    public @NonNull TaskModel update(@NonNull UUID key, @NonNull Long version, @NonNull UpdateTaskRequest request) {
        return taskRestMapper.toModel(domainService.update(key, version, taskRestMapper.toDTO(request)));
    }

    public void delete(@NonNull UUID key, @NonNull Long version) {
        domainService.delete(key, version);
    }
}
