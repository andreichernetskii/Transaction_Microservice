package com.transaction_microservice;

import com.transaction_microservice.exception.EmptyTransactionDtoException;
import com.transaction_microservice.exception.TransactionEntityNotFoundException;
import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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

    @Test
    void addTransactionTest_SuccessAdd() {
        TransactionDto transactionDto = createTransactionDtoObjectIdIsNull();
        TransactionEntity transactionEntity = createTransactionEntityObject();

        when( transactionToDtoMapper.transactionDtoToTransaction( transactionDto ) ).thenReturn( createTransactionObject() );
        when( transactionToEntityMapper.transactionToTransactionEntity( any( Transaction.class ) ) ).thenReturn( transactionEntity );

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

    @Test
    void addTransactionTest_ThrowEmptyTransactionDtoException() {
        TransactionDto transactionDtoIdIsNull = createTransactionDtoObjectIdIsNull();
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionService.addTransaction( transactionDtoIdIsNull ) );

        TransactionDto transactionDtoNull = null;
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionService.addTransaction( transactionDtoNull ) );
    }

    private TransactionDto createTransactionDtoObjectIdIsNull() {
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
    void getAllTransactionsOrByCriteria() {
    }

    @Test
    void deleteTransactionTest() {
        Long transactionId = 1L;
        doNothing().when( transactionRepository ).deleteById( transactionId );
        transactionRepository.deleteById( transactionId );
        verify( transactionRepository, times( 1 ) ).deleteById( transactionId );
    }

    @Test
    void updateTransactionTest_SuccessUpdate() {
        TransactionDto transactionDto = createTransactionDtoObjectIdIsNull();
        transactionDto.setId( 1L );
        TransactionEntity transactionEntity = createTransactionEntityObject();

        when( transactionRepository.findById( transactionDto.getId() ) ).thenReturn( Optional.of( transactionEntity ) );
        transactionDto.setCategory( "Games" );
        transactionEntity.setCategory( "Games" );

        when( transactionToDtoMapper.transactionDtoToTransaction( transactionDto ) ).thenReturn( createTransactionObject() );
        when( transactionToEntityMapper.transactionToTransactionEntity( any( Transaction.class ) ) ).thenReturn( transactionEntity );

        defaultTransactionService.updateTransaction( transactionDto );

        ArgumentCaptor<TransactionEntity> transactionEntityArgumentCaptor = ArgumentCaptor.forClass( TransactionEntity.class );
        // capturing saved file
        verify( transactionRepository ).save( transactionEntityArgumentCaptor.capture() );
        // put data from captured file to the new TransactionEntity
        TransactionEntity capturedTransactionEntity = transactionEntityArgumentCaptor.getValue();

        assertEquals( transactionDto.getId(), capturedTransactionEntity.getId() );
        assertEquals( transactionDto.getCategory(), capturedTransactionEntity.getCategory() );
    }

    @Test
    void updateTransactionTest_ThrowEmptyTransactionDtoException() {
        TransactionDto transactionDtoIdIsNull = createTransactionDtoObjectIdIsNull();
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionService.updateTransaction( transactionDtoIdIsNull ) );

        TransactionDto transactionDtoNull = null;
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionService.updateTransaction( transactionDtoNull ) );
    }

    @Test
    void updateTransactionTest_ThrowTransactionEntityNotFoundException() {
        TransactionDto transactionDto = createTransactionDtoObjectIdIsNull();
        transactionDto.setId( 2L );

        when( transactionRepository.findById( transactionDto.getId() ) ).thenReturn( Optional.empty() );
        assertThrows( TransactionEntityNotFoundException.class,
                () -> defaultTransactionService.updateTransaction( transactionDto ) );
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