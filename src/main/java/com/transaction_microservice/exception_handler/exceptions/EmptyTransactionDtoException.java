package com.transaction_microservice.exception_handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class EmptyTransactionDtoException extends RuntimeException {
    public EmptyTransactionDtoException( String message ) {
        super( message );
    }
}
