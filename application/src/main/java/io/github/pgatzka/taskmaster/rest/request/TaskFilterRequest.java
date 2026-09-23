package io.github.pgatzka.taskmaster.rest.request;

import io.github.pgatzka.taskmaster.domain.Range;

import java.time.Instant;

public record TaskFilterRequest(Range<Instant> createdAt, Range<Instant> updatedAt, String title, Boolean done) {
}
