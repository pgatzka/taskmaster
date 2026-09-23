package io.github.pgatzka.taskmaster.rest.mapper;

import io.github.pgatzka.taskmaster.domain.dto.TaskDTO;
import io.github.pgatzka.taskmaster.domain.probe.TaskProbe;
import io.github.pgatzka.taskmaster.rest.model.TaskModel;
import io.github.pgatzka.taskmaster.rest.request.CreateTaskRequest;
import io.github.pgatzka.taskmaster.rest.request.TaskFilterRequest;
import io.github.pgatzka.taskmaster.rest.request.UpdateTaskRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationPackage = "io.github.pgatzka.taskmaster.rest.mapper.impl")
public interface TaskRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "done", ignore = true)
    TaskDTO toDTO(CreateTaskRequest request);

    TaskProbe toProbe(TaskFilterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    TaskDTO toDTO(UpdateTaskRequest request);

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    TaskModel toModel(TaskDTO dto);

}
