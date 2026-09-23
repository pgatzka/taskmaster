package io.github.pgatzka.taskmaster.domain.exception;

public class UniqueConflictException extends DomainException {

    public UniqueConflictException(String message) {
        super(message);
    }

    public UniqueConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}
