package io.github.pgatzka.taskmaster.domain.exception;

public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String message) {
        super(message);
    }

}
