package com.transaction_microservice.exception_handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND )
public class TransactionEntityNotFoundException extends RuntimeException {
    public TransactionEntityNotFoundException( String message ) {
        super( message );
    }
}
