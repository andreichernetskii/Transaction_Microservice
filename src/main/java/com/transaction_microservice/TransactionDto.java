package com.transaction_microservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String category;
    private LocalDate creationDate;
}
