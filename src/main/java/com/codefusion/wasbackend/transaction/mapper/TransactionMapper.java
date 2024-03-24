package com.codefusion.wasbackend.transaction.mapper;

import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.user.mapper.LongIdMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = LongIdMapper.class)
public interface TransactionMapper {
    TransactionEntity toEntity(TransactionDTO transactionDTO);

    TransactionDTO toDto(TransactionEntity transactionEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionEntity partialUpdate(TransactionDTO transactionDTO, @MappingTarget TransactionEntity transactionEntity);
}
