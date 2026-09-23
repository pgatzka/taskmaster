package io.github.pgatzka.taskmaster.rest.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateTaskRequest(
        @NotBlank @Length(max = 255) String title
) {
}
