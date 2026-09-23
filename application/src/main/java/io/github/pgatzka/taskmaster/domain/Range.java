package io.github.pgatzka.taskmaster.domain;

public record Range<T extends Comparable<T>>(T min, T max) {
}
