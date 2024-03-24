package com.codefusion.wasbackend.store.mapper;


import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class})
public interface StoreMapper {

    StoreEntity toEntity(StoreDTO storeDTO);

    StoreDTO toDto(StoreEntity storeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    StoreEntity partialUpdate(StoreDTO storeDTO, @MappingTarget StoreEntity storeEntity);
}
