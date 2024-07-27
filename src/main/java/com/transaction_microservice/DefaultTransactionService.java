package com.transaction_microservice;

import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultTransactionService implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionToEntityMapper entityMapper;
    private final TransactionToDtoMapper dtoMapper;

    @Override
    public void addTransaction( TransactionDto transactionDto ) {
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
    public List<Transaction> getAllTransactionsOrByCriteria( Integer year,
                                                             Integer month,
                                                             TransactionType transactionType,
                                                             String category ) {

        Iterable<TransactionEntity> iterable = transactionRepository.findOperationsByCriteria(
                year,
                month,
                transactionType,
                category );

        List<Transaction> transactions = new ArrayList<>();

        for ( TransactionEntity entity : iterable ) {
            transactions.add( entityMapper.transactionEntityToTransaction( entity ) );
        }

        return transactions;
    }

    @Override
    public void deleteTransaction( Long transactionId ) {
        transactionRepository.deleteById( transactionId );
    }

    @Override
    public void updateTransaction( TransactionDto transactionDto ) {
        if ( transactionDto == null || transactionDto.getId() == null ) {
            //todo: create a dedicated exception
            return;
        }

        if ( transactionRepository.findById( transactionDto.getId() ).isEmpty() ) {
            //todo: create a dedicated exception
            throw new RuntimeException( "Entity with ID " + transactionDto.getId() + " does not exist!" );
        }

        Transaction transaction = dtoMapper.transactionDtoToTransaction( transactionDto );
        transactionRepository.save( entityMapper.transactionToTransactionEntity( transaction ) );
    }

    @Override
    public BigDecimal getAnnualBalance( Integer year,
                                        Integer month,
                                        TransactionType transactionType,
                                        String category ) {
        return null;
    }

    @Override
    public List<String> getTransactionCategories() {
        return null;
    }

    @Override
    public List<String> getTransactionTypes() {
        return null;
    }
}
