package com.transaction_microservice;

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
    private final TransactionToEntityMapper mapper;

    @Override
    public ResponseEntity<?> addTransaction( Transaction transaction ) {
        // Adjust the amount based on the operation type (expense or income)
        BigDecimal amount = ( transaction.getTransactionType() == TransactionType.EXPENSE )
                ? transaction.getAmount().negate()
                : transaction.getAmount();

        TransactionEntity transactionEntity = mapper.transactionToTransactionEntity( transaction );
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
            transactions.add( mapper.transactionEntityToTransaction( entity ) );
        }

        return transactions;
    }

    @Override
    public ResponseEntity<?> deleteTransaction( Long transactionId ) {

        transactionRepository.deleteById( transactionId );

        return ResponseEntity.ok( "Deleted" );
    }

    @Override
    public ResponseEntity<?> updateTransaction( Transaction transaction ) {
        TransactionEntity transactionEntity = transactionRepository.findById( transaction.getId() ).orElseThrow();
        transactionRepository.save( mapper.transactionToTransactionEntity( transaction ) );

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
