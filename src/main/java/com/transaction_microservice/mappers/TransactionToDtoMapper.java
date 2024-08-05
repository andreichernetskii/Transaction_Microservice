package com.transaction_microservice.mappers;

import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDto;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring" )
public interface TransactionToDtoMapper {
    Transaction transactionDtoToTransaction( TransactionDto transactionDto );
}
