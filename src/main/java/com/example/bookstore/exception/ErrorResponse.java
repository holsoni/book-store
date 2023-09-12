package com.example.bookstore.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private List<String> errors;
    private HttpStatus status;
    private LocalDateTime timestamp;
}
