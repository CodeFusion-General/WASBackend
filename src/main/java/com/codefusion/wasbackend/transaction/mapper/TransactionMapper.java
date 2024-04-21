package com.codefusion.wasbackend.transaction.mapper;

import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    /**
     * Converts a TransactionDTO object to a TransactionEntity object.
     *
     * @param transactionDTO the TransactionDTO object to convert
     * @return the converted TransactionEntity object
     */
    TransactionEntity toEntity(TransactionDTO transactionDTO);

    /**
     * Converts a TransactionEntity object to a TransactionDTO object.
     *
     * @param transactionEntity the TransactionEntity object to convert
     * @return the converted TransactionDTO object
     */
    TransactionDTO toDto(TransactionEntity transactionEntity);

    /**
     * Partially updates the given TransactionEntity object with the values from the TransactionDTO object,
     * ignoring the null values.
     *
     * @param transactionDTO        the TransactionDTO object representing the updated values
     * @param transactionEntity     the TransactionEntity object to be updated
     * @return the updated TransactionEntity object
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionEntity partialUpdate(TransactionDTO transactionDTO, @MappingTarget TransactionEntity transactionEntity);
}
