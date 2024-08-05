package com.transaction_microservice.mappers;

import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionEntity;
import com.transaction_microservice.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith( MockitoExtension.class )
class TransactionToEntityMapperTest {
    @InjectMocks
    private TransactionToEntityMapperImpl mapper;

    @Test
    void transactionToTransactionEntity() {
        Transaction transaction = createTransactionObject();
        TransactionEntity transactionEntity = mapper.transactionToTransactionEntity( transaction );

        assertNotNull( transactionEntity );
        assertEquals( transaction.getId(), transactionEntity.getId() );
        assertEquals( transaction.getAmount(), transactionEntity.getAmount() );
        assertEquals( transaction.getTransactionType(), transactionEntity.getTransactionType() );
        assertEquals( transaction.getCategory(), transactionEntity.getCategory() );
        assertEquals( transaction.getCreationDate(), transactionEntity.getCreationDate() );
    }

    private Transaction createTransactionObject() {
        return Transaction
                .builder()
                .id( 1L )
                .amount( new BigDecimal( 100 ) )
                .transactionType( TransactionType.INCOME )
                .category( "Car" )
                .creationDate( LocalDate.now() )
                .build();
    }

    @Test
    void transactionEntityToTransaction() {
        TransactionEntity transactionEntity = createTransactionEntityObject();
        Transaction transaction = mapper.transactionEntityToTransaction( transactionEntity );

        assertNotNull( transaction );
        assertEquals( transactionEntity.getId(), transaction.getId() );
        assertEquals( transactionEntity.getAmount(), transaction.getAmount() );
        assertEquals( transactionEntity.getTransactionType(), transaction.getTransactionType() );
        assertEquals( transactionEntity.getCategory(), transaction.getCategory() );
        assertEquals( transactionEntity.getCreationDate(), transaction.getCreationDate() );
    }

    private TransactionEntity createTransactionEntityObject() {
        return TransactionEntity
                .builder()
                .id( 1L )
                .amount( new BigDecimal( 100 ) )
                .transactionType( TransactionType.INCOME )
                .category( "Car" )
                .creationDate( LocalDate.now() )
                .build();
    }
}