package com.transaction_microservice.repository;

import com.transaction_microservice.enums.TransactionType;
import com.transaction_microservice.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    // Custom query to find financial transactions based on specified criteria.
    @Query( """
            SELECT transaction 
            FROM TransactionEntity transaction
            WHERE ( :yearParam IS NULL OR YEAR( transaction.creationDate ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( transaction.creationDate ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR transaction.transactionType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR transaction.category = :categoryParam )
            """ )
    List<TransactionEntity> findOperationsByCriteria( /*@Param( "accountId" ) Long accountId,*/
                                                @Param( "yearParam" ) Integer year,
                                                @Param( "monthParam" ) Integer month,
                                                @Param( "operationTypeParam" ) TransactionType transactionType,
                                                @Param( "categoryParam" ) String category );

    // Custom query to calculate the annual balance based on specified criteria.
    @Query( """
            SELECT SUM( transaction.amount ) 
            FROM TransactionEntity transaction 
            WHERE ( :yearParam IS NULL OR YEAR( transaction.creationDate ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( transaction.creationDate ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR transaction.transactionType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR transaction.category = :categoryParam )
            """ )
    BigDecimal calculateBalanceByCriteria( /*@Param( "accountId" ) Long accountId,*/
                                             @Param( "yearParam" ) Integer year,
                                             @Param( "monthParam" ) Integer month,
                                             @Param( "operationTypeParam" ) TransactionType transactionType,
                                             @Param( "categoryParam" ) String category );

    // Custom query to retrieve a list of categories for a specific account.
    @Query( """
            SELECT transaction.category 
            FROM TransactionEntity transaction
            GROUP BY transaction.category 
            ORDER BY transaction.category
            """ )
    List<String> getCategories( /*@Param( "accountId" ) Long accountId*/ );

    // Custom query to calculate monthly expenses for a given month.
    @Query( """
            SELECT SUM( transaction.amount )
            FROM TransactionEntity transaction
            WHERE transaction.transactionType = 'EXPENSE'
            AND YEAR( transaction.creationDate ) = YEAR( :monthParam )
            AND MONTH( transaction.creationDate ) = MONTH( :monthParam )
            """ )
    Double calculateMonthExpenses( @Param( "monthParam" ) LocalDate month );

    // Custom query to calculate yearly expenses for a given year.
    @Query( """
            SELECT SUM( transaction.amount )
            FROM TransactionEntity  transaction
            WHERE transaction.transactionType = 'EXPENSE'
            AND YEAR( transaction.creationDate ) = YEAR( :yearParam )
            """ )
    Double calculateYearExpenses( @Param( "yearParam" ) LocalDate year );

    // Custom query to calculate daily expenses for a given day.
    @Query( """
            SELECT SUM( transaction.amount )
            FROM TransactionEntity transaction
            WHERE transaction.transactionType = 'EXPENSE'
            AND YEAR( transaction.creationDate ) = YEAR( :dayParam )
            AND MONTH( transaction.creationDate ) = MONTH( :dayParam )
            AND DAY( transaction.creationDate ) = DAY( :dayParam )
            """ )
    Double calculateDayExpenses( @Param( "dayParam" ) LocalDate day );

    // Custom query to calculate weekly expenses for a given week.
    @Query( """
            SELECT SUM( transaction.amount )
            FROM TransactionEntity transaction
            WHERE transaction.transactionType = 'EXPENSE'
            AND ( :firstWeekDayParam IS NULL OR transaction.creationDate >= :firstWeekDayParam) 
            AND ( :lastWeekDayParam IS NULL OR transaction.creationDate <= :lastWeekDayParam)
            """ )
    Double calculateWeekExpenses( @Param( "firstWeekDayParam" ) LocalDate firstWeekDay,
                                  @Param( "lastWeekDayParam" ) LocalDate lastWeekDay );
}
