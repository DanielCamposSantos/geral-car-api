package io.github.danielcampossantos.geralcar.exception.dto;

import lombok.Builder;

@Builder
public record DefaultErrorMessage(
        int status,
        String message
) {
}
