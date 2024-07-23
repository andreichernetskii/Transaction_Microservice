package com.transaction_microservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith( MockitoExtension.class )
class DefaultTransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private DefaultTransactionService defaultTransactionService;

    @Test
    void addTransaction_Success() {
        TransactionDto transactionDto = createTransactionDto();

        ResponseEntity<?> responseEntity = defaultTransactionService.addTransaction( transactionDto );

        ArgumentCaptor<TransactionEntity> transactionEntityArgumentCaptor = ArgumentCaptor.forClass( TransactionEntity.class );
        // capturing saved file
        verify( transactionRepository ).save( transactionEntityArgumentCaptor.capture() );
        // put data from captured file to the new TransactionEntity
        TransactionEntity capturedTransactionEntity = transactionEntityArgumentCaptor.getValue();

        // assertions
        assertEquals( transactionDto.getAmount(), capturedTransactionEntity.getAmount() );
        assertEquals( transactionDto.getTransactionType(), capturedTransactionEntity.getTransactionType() );
        assertEquals( transactionDto.getCategory(), capturedTransactionEntity.getCategory() );
        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
    }

    private TransactionDto createTransactionDto() {
        return new TransactionDto(
                null,
                new BigDecimal( 100 ),
                TransactionType.INCOME,
                "car",
                LocalDate.now()
        );
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