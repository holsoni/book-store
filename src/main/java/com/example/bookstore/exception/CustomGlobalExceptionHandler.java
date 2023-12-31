package com.example.bookstore.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                        .map(this::getErrorMessage)
                                .toList();
        ErrorResponse response = new ErrorResponse(
                errors,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(RegistrationException.class)
    protected ErrorResponse handleUserAlreadyRegistered(RegistrationException ex) {
        return new ErrorResponse(
                List.of(ex.getMessage()),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                List.of(ex.getMessage()),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }
}
