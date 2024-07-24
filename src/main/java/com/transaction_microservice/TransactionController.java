package com.transaction_microservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/transactions" )
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;


    @GetMapping( "/" )
    public List<Transaction> getAllTransactionOrByCriteria( @RequestParam( name = "year", required = false ) Integer year,
                                                            @RequestParam( name = "month", required = false ) Integer month,
                                                            @RequestParam( name = "transactionType", required = false ) TransactionType transactionType,
                                                            @RequestParam( name = "category", required = false ) String category ) {

        return transactionService.getAllTransactionsOrByCriteria( year, month, transactionType, category );
    }

    @DeleteMapping( "/{transactionId}" )
    public void deleteTransaction( @PathVariable( "transactionId" ) Long transactionId ) {
        transactionService.deleteTransaction( transactionId );
    }

    @PutMapping( "/" )
    public void updateTransaction( @RequestBody TransactionDto transactionDto ) {
        transactionService.updateTransaction( transactionDto );
    }

    @PostMapping( "/" )
    public void addTransaction( @RequestBody TransactionDto transactionDto) {
        transactionService.addTransaction( transactionDto );
    }
}
