package ru.fishexam.fishexam.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fishexam.fishexam.exceptionHandler.error.BaseError;
import ru.fishexam.fishexam.exceptionHandler.exception.BadRequestException;

@RestControllerAdvice
public class MainRestControllerAdvice {

    private static final String MESSAGE_TEMPLATE_500 = "Some backend problem with service";

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseError> handleIllegalArgumentException(
            Exception ex,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new BaseError(MESSAGE_TEMPLATE_500, request.getServletPath()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseError> handleBadRequestException(
            BadRequestException exception,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new BaseError(exception.getMessage(), request.getServletPath()),
                HttpStatus.BAD_REQUEST
        );
    }
}