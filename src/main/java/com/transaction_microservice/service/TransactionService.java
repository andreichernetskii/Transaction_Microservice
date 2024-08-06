package com.transaction_microservice.service;

import com.transaction_microservice.enums.TransactionType;
import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void addTransaction( TransactionDto transactionDto );

    // todo: refacto to call by user id
    List<Transaction> getAllTransactionsOrByCriteria( Integer year,
                                                      Integer month,
                                                      TransactionType transactionType,
                                                      String category );

    void deleteTransaction( Long transactionId );

    void updateTransaction( TransactionDto transactionDto );

    BigDecimal getBalance( Integer year,
                           Integer month,
                           TransactionType transactionType,
                           String category );

    List<String> getTransactionCategories();

    List<String> getTransactionTypes();
}