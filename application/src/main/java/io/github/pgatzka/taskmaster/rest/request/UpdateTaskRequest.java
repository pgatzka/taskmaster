package io.github.pgatzka.taskmaster.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UpdateTaskRequest(
        @NotBlank @Length(max = 255) String title,
        @NotNull Boolean done
) {
}
