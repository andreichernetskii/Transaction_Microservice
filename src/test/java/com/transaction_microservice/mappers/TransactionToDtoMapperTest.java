package com.transaction_microservice.mappers;

import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDto;
import com.transaction_microservice.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith( MockitoExtension.class )
class TransactionToDtoMapperTest {
    @InjectMocks
    private TransactionToDtoMapperImpl mapper;

    @Test
    void transactionDtoToTransaction() {
        TransactionDto transactionDto = createTransactionDtoObject();
        Transaction transaction = mapper.transactionDtoToTransaction( transactionDto );

        assertNotNull( transaction );
        assertEquals( transactionDto.getAmount(), transaction.getAmount() );
        assertEquals( transactionDto.getTransactionType(), transaction.getTransactionType() );
        assertEquals( transactionDto.getCategory(), transaction.getCategory() );
        assertEquals( transactionDto.getCreationDate(), transaction.getCreationDate() );
    }

    private TransactionDto createTransactionDtoObject() {
        return TransactionDto
                .builder()
                .amount( new BigDecimal( 100 ) )
                .transactionType( TransactionType.INCOME )
                .category( "Car" )
                .creationDate( LocalDate.now() )
                .build();
    }

}