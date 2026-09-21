package io.github.pgatzka.taskmaster.domain.exception;

import io.github.pgatzka.taskmaster.domain.entity.TaskEntity;

import java.util.UUID;

public class TaskNotFoundException extends EntityNotFoundException {

    public TaskNotFoundException(UUID id){
        super(TaskEntity.class, id);
    }

}
