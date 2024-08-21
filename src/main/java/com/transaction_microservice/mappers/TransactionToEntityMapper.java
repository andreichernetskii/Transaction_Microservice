package com.transaction_microservice.mappers;

import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring" )
public interface TransactionToEntityMapper {
    TransactionEntity transactionToTransactionEntity( Transaction transaction );
    Transaction transactionEntityToTransaction( TransactionEntity transactionEntity );
}
