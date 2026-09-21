package io.github.pgatzka.taskmaster.domain.exception;

public class UniqueConstraintViolationException extends EntityException {

    public UniqueConstraintViolationException(String message) {
        super(message);
    }

}
