package io.github.pgatzka.taskmaster.domain.exception;

public class VersionMismatchException extends DomainException {
    public VersionMismatchException(String message) {
        super(message);
    }

    public VersionMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
