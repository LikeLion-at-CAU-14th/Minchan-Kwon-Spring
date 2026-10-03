package com.example.likelion14th_springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), e.getMessage()));
    }

    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateNameException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), e.getMessage()));
    }
}