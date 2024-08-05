package com.transaction_microservice.model;

import com.transaction_microservice.enums.TransactionType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class Transaction {
    Long id;
    BigDecimal amount;
    TransactionType transactionType;
    String category;
    LocalDate creationDate;
}
