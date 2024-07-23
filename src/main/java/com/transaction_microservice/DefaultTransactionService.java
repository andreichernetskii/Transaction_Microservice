package com.transaction_microservice;

import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addTransaction( TransactionDto transactionDto ) {
        // Adjust the amount based on the operation type (expense or income)
        BigDecimal amount = ( transactionDto.getTransactionType() == TransactionType.EXPENSE )
                ? transactionDto.getAmount().negate()
                : transactionDto.getAmount();

        Transaction transaction = dtoMapper.transactionDtoToTransaction( transactionDto );
        TransactionEntity transactionEntity = entityMapper.transactionToTransactionEntity( transaction );
        transactionEntity.setAmount( amount );

        transactionRepository.save( transactionEntity );

        return ResponseEntity.ok( "Ok" );
    }

    // todo: am I need this???
    @Override
    public Transaction getTransaction( Long transactionId ) {
        return null;
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
    public ResponseEntity<?> deleteTransaction( Long transactionId ) {

        transactionRepository.deleteById( transactionId );

        return ResponseEntity.ok( "Deleted" );
    }

    @Override
    public ResponseEntity<?> updateTransaction( TransactionDto transactionDto ) {

        if ( transactionDto == null || transactionDto.getId() == null ) {
            return ResponseEntity.badRequest().body( "Invalid transaction data: transaction cannot be a null" );
        }

        TransactionEntity transactionEntity = transactionRepository.findById( transactionDto.getId() )
                .orElseThrow(
                        () -> new RuntimeException( "Entity with ID " + transactionDto.getId() + " does not exist!")
                );

        Transaction transaction = dtoMapper.transactionDtoToTransaction( transactionDto );
        transactionRepository.save( entityMapper.transactionToTransactionEntity( transaction ) );

        return ResponseEntity.ok( "Updated" );
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
