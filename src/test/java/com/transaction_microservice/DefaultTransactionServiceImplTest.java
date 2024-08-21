package com.transaction_microservice;

import com.transaction_microservice.enums.TransactionType;
import com.transaction_microservice.exception_handler.exceptions.EmptyTransactionDtoException;
import com.transaction_microservice.exception_handler.exceptions.TransactionEntityNotFoundException;
import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDto;
import com.transaction_microservice.entity.TransactionEntity;
import com.transaction_microservice.repository.TransactionRepository;
import com.transaction_microservice.service.DefaultTransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@ExtendWith( MockitoExtension.class )
class DefaultTransactionServiceImplTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionToDtoMapper transactionToDtoMapper;
    @Mock
    private TransactionToEntityMapper transactionToEntityMapper;
    @Mock
    UserDetails userDetails;
    @InjectMocks
    private DefaultTransactionServiceImpl defaultTransactionServiceImpl;

    @Test
    void addTransactionTest_SuccessAdd() {
        TransactionDto transactionDto = createTransactionDtoObjectIdIsNull();
        TransactionEntity transactionEntity = createTransactionEntityObject();

        when( userDetails.getUsername() ).thenReturn( "88df8aasd88a2" );
        when( transactionToDtoMapper.transactionDtoToTransaction( transactionDto ) ).thenReturn( createTransactionObject() );
        when( transactionToEntityMapper.transactionToTransactionEntity( any( Transaction.class ) ) ).thenReturn( transactionEntity );

        defaultTransactionServiceImpl.addTransaction( userDetails.getUsername(), transactionDto );

        ArgumentCaptor<TransactionEntity> transactionEntityArgumentCaptor = ArgumentCaptor.forClass( TransactionEntity.class );
        // capturing saved file
        verify( transactionRepository ).save( transactionEntityArgumentCaptor.capture() );
        // put data from captured file to the new TransactionEntity
        TransactionEntity capturedTransactionEntity = transactionEntityArgumentCaptor.getValue();

        assertEquals( transactionDto.getAmount(), capturedTransactionEntity.getAmount() );
        assertEquals( transactionDto.getTransactionType(), capturedTransactionEntity.getTransactionType() );
        assertEquals( transactionDto.getCategory(), capturedTransactionEntity.getCategory() );
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
        List<Transaction> transactions = new ArrayList<>(
                Arrays.asList( createTransactionObject(), createTransactionObject() )
        );

        List<TransactionEntity> transactionEntities = new ArrayList<>(
                Arrays.asList( createTransactionEntityObject(), createTransactionEntityObject() )
        );

        when( transactionRepository.findOperationsByCriteria( null, null, null, null, null ) )
                .thenReturn( transactionEntities );
        when( transactionToEntityMapper.transactionEntityToTransaction( any( TransactionEntity.class ) ) )
                .thenReturn( Transaction.builder().build() );

        List<Transaction> result = defaultTransactionServiceImpl.getAllTransactionsOrByCriteria( null, null, null, null, null );

        assertEquals( transactions.size(), result.size() );
        verify( transactionRepository, times( 1 ) ).findOperationsByCriteria( null, null, null, null, null );

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

        when( userDetails.getUsername() ).thenReturn( "88df8aasd88a2" );

        when( transactionRepository.findById( transactionDto.getId() ) ).thenReturn( Optional.of( transactionEntity ) );
        transactionDto.setCategory( "Games" );
        transactionEntity.setCategory( "Games" );

        when( transactionToDtoMapper.transactionDtoToTransaction( transactionDto ) ).thenReturn( createTransactionObject() );
        when( transactionToEntityMapper.transactionToTransactionEntity( any( Transaction.class ) ) ).thenReturn( transactionEntity );

        defaultTransactionServiceImpl.updateTransaction( userDetails.getUsername(), transactionDto );

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
        when( userDetails.getUsername() ).thenReturn( "88df8aasd88a2" );

        TransactionDto transactionDtoIdIsNull = createTransactionDtoObjectIdIsNull();
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionServiceImpl.updateTransaction( userDetails.getUsername(), transactionDtoIdIsNull ) );

        TransactionDto transactionDtoNull = null;
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionServiceImpl.updateTransaction( userDetails.getUsername(), transactionDtoNull ) );
    }

    @Test
    void updateTransactionTest_ThrowTransactionEntityNotFoundException() {
        when( userDetails.getUsername() ).thenReturn( "88df8aasd88a2" );

        TransactionDto transactionDto = createTransactionDtoObjectIdIsNull();
        transactionDto.setId( 2L );

        when( transactionRepository.findById( transactionDto.getId() ) ).thenReturn( Optional.empty() );
        assertThrows( TransactionEntityNotFoundException.class,
                () -> defaultTransactionServiceImpl.updateTransaction( userDetails.getUsername(), transactionDto ) );
    }
}