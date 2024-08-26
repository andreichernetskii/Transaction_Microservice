package com.transaction_microservice.service;

import com.transaction_microservice.enums.TransactionType;
import com.transaction_microservice.exception_handler.exceptions.EmptyTransactionDtoException;
import com.transaction_microservice.exception_handler.exceptions.TransactionEntityNotFoundException;
import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDto;
import com.transaction_microservice.entity.TransactionEntity;
import com.transaction_microservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultTransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionToEntityMapper entityMapper;
    private final TransactionToDtoMapper dtoMapper;

    @Override
    public void addTransaction( String userId, TransactionDto transactionDto ) {
        transactionDto.setUserId( userId );
        BigDecimal amount = getBigDecimalWithSign( transactionDto );
        Transaction transaction = dtoMapper.transactionDtoToTransaction( transactionDto );
        TransactionEntity transactionEntity = entityMapper.transactionToTransactionEntity( transaction );

        transactionEntity.setAmount( amount );
        transactionRepository.save( transactionEntity );
    }

    private BigDecimal getBigDecimalWithSign( TransactionDto transactionDto ) {
        return ( transactionDto.getTransactionType() == TransactionType.EXPENSE )
                ? transactionDto.getAmount().negate()
                : transactionDto.getAmount();
    }

    @Override
    public void updateTransaction( String userId, TransactionDto transactionDto ) {
        checkTransactionDtoNotNull( transactionDto );
        checkTransactionExists( transactionDto );

        Transaction transaction = dtoMapper.transactionDtoToTransaction( transactionDto );
        transactionRepository.save( entityMapper.transactionToTransactionEntity( transaction ) );
    }

    private void checkTransactionDtoNotNull( TransactionDto transactionDto ) {
        if ( transactionDto == null || transactionDto.getId() == null ) {
            throw new EmptyTransactionDtoException( "TransactionDto is null!" );
        }
    }

    private void checkTransactionExists( TransactionDto transactionDto ) {
        if ( transactionRepository.findById( transactionDto.getId() ).isEmpty() ) {
            throw new TransactionEntityNotFoundException( "Entity with ID " + transactionDto.getId() + " does not exist!" );
        }
    }

    @Override
    public List<Transaction> getAllTransactionsOrByCriteria( String userId,
                                                             Integer year,
                                                             Integer month,
                                                             TransactionType transactionType,
                                                             String category ) {

        return transactionRepository.findOperationsByCriteria(
                    userId,
                    year,
                    month,
                    transactionType,
                    category )
                .stream()
                .map( entityMapper::transactionEntityToTransaction )
                .toList();
    }

    @Transactional
    @Override
    public void deleteTransaction( String userId, Long transactionId ) {
        if ( transactionRepository.findById( transactionId ).isEmpty() ) {
            throw new TransactionEntityNotFoundException( "Entity with ID " + transactionId + " does not exist!" );
        }
        transactionRepository.deleteById( transactionId );
    }

    @Override
    public BigDecimal getBalance( String userId,
                                  Integer year,
                                  Integer month,
                                  TransactionType transactionType,
                                  String category ) {

        return transactionRepository.calculateBalanceByCriteria( userId, year, month, transactionType, category );
    }

    @Override
    public List<String> getTransactionCategories( String userId ) {
        return transactionRepository.getCategories( userId );
    }

    @Override
    public List<String> getTransactionTypes() {
        return Arrays.stream( TransactionType.values() ).map( Enum::toString ).toList();
    }
}
