package com.pkaczmarek.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InventoryExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ErrorMessage handleBookNotFoundException(BookNotFoundException ex) {
        return new ErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(RuntimeException.class)
    public ErrorMessage handleRuntimeException(RuntimeException ex) {
        return new ErrorMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception ex) {
        return new ErrorMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}