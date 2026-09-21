package io.github.pgatzka.taskmaster.domain.exception;

public class TaskTitleAlreadyExistsException extends UniqueConstraintViolationException {

    public TaskTitleAlreadyExistsException(String title) {
        super("task with title \"" + title + "\" already exists");
    }

}
