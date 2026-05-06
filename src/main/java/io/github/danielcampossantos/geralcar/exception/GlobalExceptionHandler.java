package io.github.danielcampossantos.geralcar.exception;

import io.github.danielcampossantos.geralcar.exception.dto.ApiErrorMessages;
import io.github.danielcampossantos.geralcar.exception.dto.DefaultErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DefaultErrorMessage> handleBadRequestException(BadRequestException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorMessages> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                                  HttpServletRequest request) {
        var errorMessages = e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField, FieldError::getDefaultMessage
        ));

        var apiErrorMessage = ApiErrorMessages.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(OffsetDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .messages(errorMessages)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(apiErrorMessage);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<DefaultErrorMessage> handleMissingServletRequestPartException(
            MissingServletRequestPartException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
