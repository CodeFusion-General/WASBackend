package com.codefusion.wasbackend.Category.mapper;

import com.codefusion.wasbackend.Category.dto.CategoryDto;
import com.codefusion.wasbackend.Category.model.CategoryEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryEntity toEntity(CategoryDto categoryDto);

    @AfterMapping
    default void linkPrototypes(@MappingTarget CategoryEntity categoryEntity) {
        categoryEntity.getPrototypes().forEach(prototype -> prototype.setCategory(categoryEntity));
    }

    CategoryDto toDto(CategoryEntity categoryEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoryEntity partialUpdate(CategoryDto categoryDto, @MappingTarget CategoryEntity categoryEntity);
}