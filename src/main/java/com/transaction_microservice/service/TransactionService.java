package com.transaction_microservice.service;

import com.transaction_microservice.enums.TransactionType;
import com.transaction_microservice.model.SearchCriteria;
import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDTO;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void addTransaction( String userId, List<TransactionDTO> transactionDTOList );

    List<Transaction> getAllTransactionsOrByCriteria( String userId, SearchCriteria searchCriteria );

    void deleteTransaction( String userId, Long transactionId );

    void updateTransaction( String userId, TransactionDTO transactionDto );

    BigDecimal getBalance( String userId,
                           Integer year,
                           Integer month,
                           TransactionType transactionType,
                           String category );

    List<String> getTransactionCategories( String userId );

    List<String> getTransactionTypes();
}
