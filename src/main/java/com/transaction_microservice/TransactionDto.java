package com.transaction_microservice;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String category;
    private LocalDate creationDate;
}
