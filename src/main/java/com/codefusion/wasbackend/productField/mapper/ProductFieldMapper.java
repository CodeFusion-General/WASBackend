package com.codefusion.wasbackend.productField.mapper;

import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.productField.dto.ProductFieldDTO;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class, ProductMapper.class})
public interface ProductFieldMapper {

    ProductFieldEntity toEntity(ProductFieldDTO productFieldDTO);

    ProductFieldDTO toDto(ProductFieldEntity productFieldEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductFieldEntity partialUpdate(ProductFieldDTO productFieldDTO, @MappingTarget ProductFieldEntity productFieldEntity);
}
