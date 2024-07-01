package com.transaction_microservice;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DefaultTransactionService implements TransactionService {
    @Override
    public ResponseEntity<?> addTransaction( Transaction transaction ) {
        return null;
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
