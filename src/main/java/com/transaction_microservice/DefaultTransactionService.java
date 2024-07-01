package com.transaction_microservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultTransactionService implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public ResponseEntity<?> addTransaction( Transaction transaction ) {
        // Adjust the amount based on the operation type (expense or income)
        BigDecimal amount = ( transaction.getTransactionType() == TransactionType.EXPENSE )
                ? transaction.getAmount().negate()
                : transaction.getAmount();

        transactionRepository.save( new Transaction(
                null,
                amount,
                transaction.getTransactionType(),
                transaction.getCategory(),
                transaction.getDate() ) );

//        return ResponseEntity.ok( new MessageResponse( "Financial transaction successfully added." ) );
        return ResponseEntity.ok("Ok");
    }

    @Override
    public Transaction getTransaction( Long transactionId ) {
        return null;
    }

    @Override
    public List<Transaction> getAllTransactionsOrByCriteria( Integer year, Integer month, TransactionType transactionType, String category ) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteTransaction( Long transactionId ) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateTransaction( Long transactionId ) {
        return null;
    }

    @Override
    public BigDecimal getAnnualBalance( Integer year, Integer month, TransactionType transactionType, String category ) {
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
