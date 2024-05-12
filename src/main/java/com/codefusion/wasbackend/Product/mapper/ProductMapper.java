package com.codefusion.wasbackend.product.mapper;

import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.model.ProductEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductEntity toEntity(ProductDTO productDTO);

    @AfterMapping
    default void linkProductFields(@MappingTarget ProductEntity productEntity) {
        productEntity.getProductFields().forEach(productField -> productField.setProduct(productEntity));
    }

    @AfterMapping
    default void linkTransactions(@MappingTarget ProductEntity productEntity) {
        productEntity.getTransactions().forEach(transaction -> transaction.setProduct(productEntity));
    }

    ProductDTO toDto(ProductEntity productEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductEntity partialUpdate(ProductDTO productDTO, @MappingTarget ProductEntity productEntity);
}