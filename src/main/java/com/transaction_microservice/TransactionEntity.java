package com.transaction_microservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private BigDecimal amount;
    @Enumerated( EnumType.STRING )
    private TransactionType transactionType;
    private String category;
    private LocalDate creationDate;
}
