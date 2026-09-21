package io.github.pgatzka.taskmaster.domain.exception;

public abstract class EntityException extends RuntimeException{

    public EntityException(String message) {
        super(message);
    }
}
