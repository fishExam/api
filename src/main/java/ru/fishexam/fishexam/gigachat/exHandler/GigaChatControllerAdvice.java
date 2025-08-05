package ru.fishexam.fishexam.gigachat.exHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fishexam.fishexam.exceptionHandler.error.BaseError;

@RestControllerAdvice
public class GigaChatControllerAdvice {

    @ExceptionHandler(GigaChatException.class)
    public ResponseEntity<BaseError> handleIOException(GigaChatException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new BaseError(ex.getMessage(), request.getServletPath()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}