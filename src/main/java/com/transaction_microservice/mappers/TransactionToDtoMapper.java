package com.transaction_microservice.mappers;

import com.transaction_microservice.Transaction;
import com.transaction_microservice.TransactionDto;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring" )
public interface TransactionToDtoMapper {
    Transaction transactionDtoToTransaction( TransactionDto transactionDto );
}
