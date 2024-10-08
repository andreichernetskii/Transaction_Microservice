package com.transaction_microservice.model;

import com.transaction_microservice.enums.TransactionType;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class TransactionDTO {
    @Nullable
    private Long id;
    @Nullable
    private String userId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String category;
    private LocalDate creationDate;
}
