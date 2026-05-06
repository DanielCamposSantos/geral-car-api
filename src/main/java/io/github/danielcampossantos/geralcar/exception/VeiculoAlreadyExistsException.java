package io.github.danielcampossantos.geralcar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class VeiculoAlreadyExistsException extends ResponseStatusException {
    public VeiculoAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
