package com.transaction_microservice.service;

import com.transaction_microservice.enums.TransactionType;
import com.transaction_microservice.exception_handler.exceptions.EmptyTransactionDtoException;
import com.transaction_microservice.exception_handler.exceptions.TransactionEntityNotFoundException;
import com.transaction_microservice.mappers.TransactionToDtoMapper;
import com.transaction_microservice.mappers.TransactionToEntityMapper;
import com.transaction_microservice.model.SearchCriteria;
import com.transaction_microservice.model.Transaction;
import com.transaction_microservice.model.TransactionDTO;
import com.transaction_microservice.entity.TransactionEntity;
import com.transaction_microservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DefaultTransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionToEntityMapper entityMapper;
    private final TransactionToDtoMapper dtoMapper;

    @Override
    public void addTransaction( String userId, List<TransactionDTO> transactionDTOList ) {
        List<TransactionEntity> transactionEntityList = transactionDTOList
                .stream()
                .map( dto -> {
                    dto.setUserId( userId );
                    dto.setAmount( getBigDecimalWithSign( dto ) );
                    return entityMapper.transactionToTransactionEntity(
                            dtoMapper.transactionDtoToTransaction( dto ) );
                } )
                .toList();

        transactionRepository.saveAll( transactionEntityList );
    }

    private BigDecimal getBigDecimalWithSign( TransactionDTO transactionDto ) {
        return ( transactionDto.getTransactionType() == TransactionType.EXPENSE )
                ? transactionDto.getAmount().negate()
                : transactionDto.getAmount();
    }

    @Override
    public void updateTransaction( String userId, TransactionDTO transactionDto ) {
        checkTransactionDtoNotNull( transactionDto );
        checkTransactionExists( transactionDto );

        transactionRepository.save(
                entityMapper.transactionToTransactionEntity(
                        dtoMapper.transactionDtoToTransaction( transactionDto ) ) );
    }

    private void checkTransactionDtoNotNull( TransactionDTO transactionDto ) {
        if ( transactionDto == null || transactionDto.getId() == null ) {
            throw new EmptyTransactionDtoException( "TransactionDto is null!" );
        }
    }

    private void checkTransactionExists( TransactionDTO transactionDto ) {
        if ( transactionRepository.findById( transactionDto.getId() ).isEmpty() ) {
            throw new TransactionEntityNotFoundException( "Entity with ID " + transactionDto.getId() + " does not exist!" );
        }
    }

    @Override
    public List<Transaction> getAllTransactionsOrByCriteria( String userId, SearchCriteria searchCriteria ) {

        List<TransactionEntity> resultList = (searchCriteria != null)
                ? transactionRepository.findOperationsByCriteria(
                        userId,
                        searchCriteria.getYear(),
                        searchCriteria.getMonth(),
                        searchCriteria.getTransactionType(),
                        searchCriteria.getCategory() )
                : transactionRepository.findOperationsByCriteria(
                        userId,
                        null,
                        null,
                        null,
                        null );

        return resultList.stream().map( entityMapper::transactionEntityToTransaction ).toList();
    }

    @Transactional
    @Override
    public void deleteTransaction( String userId, Long transactionId ) {
        if ( transactionRepository.findById( transactionId ).isEmpty() ) {
            throw new TransactionEntityNotFoundException( "Entity with ID " + transactionId + " does not exist!" );
        }
        transactionRepository.deleteById( transactionId );
    }

    @Override
    public BigDecimal getBalance( String userId,
                                  Integer year,
                                  Integer month,
                                  TransactionType transactionType,
                                  String category ) {

        return transactionRepository.calculateBalanceByCriteria( userId, year, month, transactionType, category );
    }

    @Override
    public List<String> getTransactionCategories( String userId ) {
        return transactionRepository.getCategories( userId );
    }

    @Override
    public List<String> getTransactionTypes() {
        return Arrays.stream( TransactionType.values() ).map( Enum::toString ).toList();
    }
}
