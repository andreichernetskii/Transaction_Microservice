package com.transaction_microservice;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    ResponseEntity<?> addTransaction( Transaction transaction ); // todo: fill the ResponseEntity by the object

    Transaction getTransaction( Long transactionId ); // todo: am I really need it?

    // todo: refacto to call by user id
    List<Transaction> getAllTransactionsOrByCriteria( Integer year,
                                                      Integer month,
                                                      TransactionType transactionType,
                                                      String category );

    ResponseEntity<?> deleteTransaction( Long transactionId );

    ResponseEntity<?> updateTransaction( Long transactionId );

    BigDecimal getAnnualBalance( Integer year,
                                 Integer month,
                                 TransactionType transactionType,
                                 String category );

    List<String> getTransactionCategories();

    List<String> getTransactionTypes();
}
