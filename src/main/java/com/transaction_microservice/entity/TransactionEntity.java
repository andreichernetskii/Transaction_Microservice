package com.transaction_microservice.entity;

import com.transaction_microservice.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private String userId;
    private BigDecimal amount;
    @Enumerated( EnumType.STRING )
    private TransactionType transactionType;
    private String category;
    private LocalDate creationDate;
}
