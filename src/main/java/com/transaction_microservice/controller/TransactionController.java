package com.transaction_microservice.controller;

import com.transaction_microservice.model.SearchCriteria;
import com.transaction_microservice.model.TransactionDTO;
import com.transaction_microservice.service.TransactionService;
import com.transaction_microservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/transactions" )
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;


    @GetMapping( "/" )
    public List<Transaction> getAllTransactionOrByCriteria( @AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody( required = false ) SearchCriteria searchCriteria ) {

        return transactionService.getAllTransactionsOrByCriteria(
                userDetails.getUsername(), searchCriteria );
    }

    @DeleteMapping( "/{transactionId}" )
    public void deleteTransaction( @AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable( "transactionId" ) Long transactionId ) {
        transactionService.deleteTransaction( userDetails.getUsername(), transactionId );
    }

    @PutMapping( "/" )
    public void updateTransaction( @AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody TransactionDTO transactionDto ) {
        transactionService.updateTransaction( userDetails.getUsername(), transactionDto );
    }

    @PostMapping( "/" )
    public void addTransaction(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<TransactionDTO> transactionDTOList ) {
        transactionService.addTransaction( userDetails.getUsername(), transactionDTOList );
    }
}
