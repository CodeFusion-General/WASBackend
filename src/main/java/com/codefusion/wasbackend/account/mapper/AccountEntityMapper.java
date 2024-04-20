package com.codefusion.wasbackend.account.mapper;

import com.codefusion.wasbackend.account.dto.AccountEntityDto;
import com.codefusion.wasbackend.account.model.AccountEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountEntityMapper {
    AccountEntity toEntity(AccountEntityDto accountEntityDto);

    AccountEntityDto toDto(AccountEntity accountEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AccountEntity partialUpdate(AccountEntityDto accountEntityDto, @MappingTarget AccountEntity accountEntity);
}