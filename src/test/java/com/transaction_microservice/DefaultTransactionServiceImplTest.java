package com.transaction_microservice;

import com.transaction_microservice.enums.TransactionType;
import com.transaction_microservice.exception_handler.exceptions.EmptyTransactionDtoException;
import com.transaction_microservice.exception_handler.exceptions.TransactionEntityNotFoundException;
import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDTO;
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
        List<TransactionDTO> transactionDTOList = List.of(
                createTransactionDtoObjectIdIsNull(),
                createTransactionDtoObjectIdIsNull()
        );

        when( userDetails.getUsername() ).thenReturn( "88df8aasd88a2" );
        when( transactionToDtoMapper.transactionDtoToTransaction( any( TransactionDTO.class ) ) ).thenReturn( createTransactionObject() );
        when( transactionToEntityMapper.transactionToTransactionEntity( any( Transaction.class ) ) ).thenReturn( createTransactionEntityObject() );

        defaultTransactionServiceImpl.addTransaction( userDetails.getUsername(), transactionDTOList );

        ArgumentCaptor<List<TransactionEntity>> transactionEntityArgumentCaptor = ArgumentCaptor.forClass( List.class );
        // capturing saved file
        verify( transactionRepository ).saveAll( transactionEntityArgumentCaptor.capture() );
        // put data from captured file to the new TransactionEntity
        List<TransactionEntity> capturedTransactionEntityList = transactionEntityArgumentCaptor.getValue();

        assertEquals( transactionDTOList.get( 0 ).getAmount(), capturedTransactionEntityList.get( 0 ).getAmount() );
        assertEquals( transactionDTOList.get( 0 ).getTransactionType(), capturedTransactionEntityList.get( 0 ).getTransactionType() );
        assertEquals( transactionDTOList.get( 0 ).getCategory(), capturedTransactionEntityList.get( 0 ).getCategory() );

        assertEquals( transactionDTOList.get( 1 ).getAmount(), capturedTransactionEntityList.get( 1 ).getAmount() );
        assertEquals( transactionDTOList.get( 1 ).getTransactionType(), capturedTransactionEntityList.get( 1 ).getTransactionType() );
        assertEquals( transactionDTOList.get( 1 ).getCategory(), capturedTransactionEntityList.get( 1 ).getCategory() );
    }

    private TransactionDTO createTransactionDtoObjectIdIsNull() {
        return TransactionDTO
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

        List<Transaction> result = defaultTransactionServiceImpl.getAllTransactionsOrByCriteria( null, null );

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
        TransactionDTO transactionDto = createTransactionDtoObjectIdIsNull();
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

        TransactionDTO transactionDTOIdIsNull = createTransactionDtoObjectIdIsNull();
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionServiceImpl.updateTransaction( userDetails.getUsername(), transactionDTOIdIsNull ) );

        TransactionDTO transactionDTONull = null;
        assertThrows( EmptyTransactionDtoException.class,
                () -> defaultTransactionServiceImpl.updateTransaction( userDetails.getUsername(), transactionDTONull ) );
    }

    @Test
    void updateTransactionTest_ThrowTransactionEntityNotFoundException() {
        when( userDetails.getUsername() ).thenReturn( "88df8aasd88a2" );

        TransactionDTO transactionDto = createTransactionDtoObjectIdIsNull();
        transactionDto.setId( 2L );

        when( transactionRepository.findById( transactionDto.getId() ) ).thenReturn( Optional.empty() );
        assertThrows( TransactionEntityNotFoundException.class,
                () -> defaultTransactionServiceImpl.updateTransaction( userDetails.getUsername(), transactionDto ) );
    }
}