package io.github.pgatzka.taskmaster.domain.mapper;

import io.github.pgatzka.taskmaster.domain.dto.TaskDTO;
import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper
public interface TaskMapper {

    TaskDTO toDTO(TaskEntity entity);

    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    TaskEntity toEntity(TaskDTO dto);

    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    void update(TaskDTO dto, @MappingTarget TaskEntity entity);

}
