package com.transaction_microservice.mappers;

import com.transaction_microservice.Transaction;
import com.transaction_microservice.TransactionDto;
import com.transaction_microservice.TransactionService;
import com.transaction_microservice.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/transactions" )
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionToDtoMapper mapper;

    @GetMapping( "/" )
    public List<Transaction> getAllTransactionOrByCriteria( @RequestParam( name = "year", required = false ) Integer year,
                                                            @RequestParam( name = "month", required = false ) Integer month,
                                                            @RequestParam( name = "transactionType", required = false ) TransactionType transactionType,
                                                            @RequestParam( name = "category", required = false ) String category ) {

        return transactionService.getAllTransactionsOrByCriteria( year, month, transactionType, category );
    }

    @DeleteMapping( "/{transactionId}" )
    public ResponseEntity<?> deleteTransaction( @PathVariable( "transactionId" ) Long transactionId ) {
        return transactionService.deleteTransaction( transactionId );
    }

    @PutMapping( "/" )
    public ResponseEntity<?> updateTransaction( @RequestBody TransactionDto transactionDto ) {
        return transactionService.updateTransaction( mapper.transactionDtoToTransaction( transactionDto ) );
    }

    @PostMapping( "/" )
    public ResponseEntity<?> addTransaction( @RequestBody TransactionDto transactionDto) {
        return transactionService.addTransaction( mapper.transactionDtoToTransaction( transactionDto ) );
    }
}
