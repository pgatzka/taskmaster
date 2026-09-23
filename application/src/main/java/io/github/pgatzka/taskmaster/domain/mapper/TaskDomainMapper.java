package io.github.pgatzka.taskmaster.domain.mapper;

import io.github.pgatzka.taskmaster.domain.dto.TaskDTO;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(implementationPackage = "io.github.pgatzka.taskmaster.domain.mapper.impl")
public interface TaskDomainMapper {

    TaskDTO toDTO(TaskEntity entity);

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "key", "createdAt", "updatedAt", "version"})
    TaskEntity toEntity(TaskDTO dto);

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "key", "createdAt", "updatedAt", "version"})
    void update(TaskDTO dto, @MappingTarget TaskEntity entity);

}
