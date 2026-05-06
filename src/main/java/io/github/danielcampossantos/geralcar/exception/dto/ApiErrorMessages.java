package io.github.danielcampossantos.geralcar.exception.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record ApiErrorMessages(
        String timestamp,
        String error,
        int status,
        Map<String, String> messages,
        String path

) {
}
