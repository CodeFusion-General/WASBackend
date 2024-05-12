package com.codefusion.wasbackend.Category.service;

import com.codefusion.wasbackend.Category.dto.CategoryDto;
import com.codefusion.wasbackend.Category.mapper.CategoryMapper;
import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.Category.repository.CategoryRepository;
import com.codefusion.wasbackend.CategoryPrototype.dto.CategoryPrototypeDto;
import com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity;
import com.codefusion.wasbackend.CategoryPrototype.repository.CategoryPrototypeRepository;
import com.codefusion.wasbackend.CategoryPrototype.service.CategoryPrototypeService;
import com.codefusion.wasbackend.product.model.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryPrototypeService categoryPrototypeService;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        return categoryRepository.findAllByIsDeletedFalse().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getByCategoryId(Long id){
        CategoryEntity categoryEntity = categoryRepository.getById(id);
        if(Boolean.TRUE.equals(categoryEntity.getIsDelete())){
            throw new IllegalStateException("Category with id " + id + " is deleted");
        }
        return categoryMapper.toDto(categoryEntity);
    }

    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto, List<CategoryPrototypeDto> categoryPrototypeDtos) {
        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDto);

        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);

        categoryPrototypeService.addCategoryPrototypes(savedCategoryEntity, categoryPrototypeDtos);

        return categoryMapper.toDto(savedCategoryEntity);
    }

}