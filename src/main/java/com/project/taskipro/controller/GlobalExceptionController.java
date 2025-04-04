package com.project.taskipro.controller;

import com.project.taskipro.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e){
        ErrorResponse error = new ErrorResponse(
                e.getStatusCode().value(),
                e.getReason(),
                e.getMessage()
        );
        return new ResponseEntity<>(error, e.getStatusCode());
    }
}
