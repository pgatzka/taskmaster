package io.github.pgatzka.taskmaster.domain.exception;

import io.github.pgatzka.taskmaster.domain.AbstractEntity;

import java.util.UUID;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<? extends AbstractEntity> type, UUID id) {
        super("could not find entity of type " + type + " with id " + id);
    }
}
