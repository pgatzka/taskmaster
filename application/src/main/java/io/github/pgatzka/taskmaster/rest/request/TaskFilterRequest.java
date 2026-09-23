package io.github.pgatzka.taskmaster.rest.request;

import io.github.pgatzka.taskmaster.domain.Range;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

public record TaskFilterRequest(Range<Instant> createdAt, Range<Instant> updatedAt, @Length(max = 255) String title, Boolean done) {
}
