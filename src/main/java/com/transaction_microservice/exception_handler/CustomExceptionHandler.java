package com.transaction_microservice.exception_handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( RuntimeException.class )
    protected ResponseEntity<Object> handleConflict( RuntimeException ex, WebRequest request ) {
        String message = ex.getMessage();
        return handleExceptionInternal( ex, message, new HttpHeaders(), HttpStatus.CONFLICT, request );
    }
}
