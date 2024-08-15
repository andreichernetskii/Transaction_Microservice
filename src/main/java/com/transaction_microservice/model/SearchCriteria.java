package com.transaction_microservice.model;

import com.transaction_microservice.enums.TransactionType;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class SearchCriteria {
    @Nullable
    private Integer year;
    @Nullable
    private Integer month;
    @Nullable
    private TransactionType transactionType;
    @Nullable
    private String category;
}
