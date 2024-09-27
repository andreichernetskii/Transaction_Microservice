package com.transaction_microservice.model;

import com.transaction_microservice.enums.LimitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) class for transferring information about limits.
 */
@Data
@AllArgsConstructor
@Builder
public class LimitDTO {
    private Long id;
    private String userId;
    private LimitType limitType;
    private BigDecimal limitAmount;
    private String category;
    private LocalDate creationDate;
}
