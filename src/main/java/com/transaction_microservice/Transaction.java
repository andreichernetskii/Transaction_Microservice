package com.transaction_microservice;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class Transaction {
    Long id;
    BigDecimal amount;
    TransactionType transactionType;
    String category;
    LocalDate creationDate;
}
