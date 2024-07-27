package com.transaction_microservice;

import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith( MockitoExtension.class )
class DefaultTransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionToDtoMapper transactionToDtoMapper;
    @Mock
    private TransactionToEntityMapper transactionToEntityMapper;
    @InjectMocks
    private DefaultTransactionService defaultTransactionService;

    //todo: tests for mappers

    @Test
    void addTransaction_Success() {
        TransactionDto transactionDto = createTransactionDtoObject();
        Transaction transaction = createTransactionObject();
        TransactionEntity transactionEntity = createTransactionEntityObject();

        when( transactionToDtoMapper.transactionDtoToTransaction( transactionDto ) ).thenReturn( transaction );
        when( transactionToEntityMapper.transactionToTransactionEntity( transaction ) ).thenReturn( transactionEntity );

        defaultTransactionService.addTransaction( transactionDto );

        ArgumentCaptor<TransactionEntity> transactionEntityArgumentCaptor = ArgumentCaptor.forClass( TransactionEntity.class );
        // capturing saved file
        verify( transactionRepository ).save( transactionEntityArgumentCaptor.capture() );
        // put data from captured file to the new TransactionEntity
        TransactionEntity capturedTransactionEntity = transactionEntityArgumentCaptor.getValue();

        assertEquals( transactionDto.getAmount(), capturedTransactionEntity.getAmount() );
        assertEquals( transactionDto.getTransactionType(), capturedTransactionEntity.getTransactionType() );
        assertEquals( transactionDto.getCategory(), capturedTransactionEntity.getCategory() );
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

    @Test
    void getTransaction() {
    }

    @Test
    void getAllTransactionsOrByCriteria() {
    }

    @Test
    void deleteTransaction() {
    }

    @Test
    void updateTransaction() {
    }

    @Test
    void getAnnualBalance() {
    }

    @Test
    void getTransactionCategories() {
    }

    @Test
    void getTransactionTypes() {
    }
}