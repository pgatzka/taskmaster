package io.github.pgatzka.taskmaster.rest.exception.handler;

import io.github.pgatzka.taskmaster.domain.exception.EntityNotFoundException;
import io.github.pgatzka.taskmaster.domain.exception.UniqueConflictException;
import io.github.pgatzka.taskmaster.domain.exception.VersionMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ProblemDetail handleEntityNotFoundException(EntityNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "Entity not found", exception);
    }

    @ExceptionHandler(UniqueConflictException.class)
    ProblemDetail handleUniqueConflictException(UniqueConflictException exception) {
        return problem(HttpStatus.CONFLICT, "Unique constraint violated", exception);
    }

    @ExceptionHandler(VersionMismatchException.class)
    ProblemDetail handleVersionMismatchException(VersionMismatchException exception) {
        return problem(HttpStatus.CONFLICT, "Version mismatch", exception);
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleExpected(Exception exception) {
        log.error("unhandled exception", exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "an unexpected error occurred");
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        ProblemDetail body = exception.getBody();
        body.setProperty("errors", exception.getFieldErrors().stream().map(fieldError -> Map.of("field", fieldError.getField(), "message", Objects.requireNonNullElse(fieldError.getDefaultMessage(), ""))).toList());
        return handleExceptionInternal(exception, body, headers, status, request);
    }

    private ProblemDetail problem(HttpStatus status, String title, Exception exception) {
        log.debug("{}: {}", title, exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        problemDetail.setTitle(title);
        return problemDetail;
    }

}
