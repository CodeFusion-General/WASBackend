package com.codefusion.wasbackend.productField.service;

import com.codefusion.wasbackend.productField.dto.ProductFieldDTO;
import com.codefusion.wasbackend.productField.mapper.ProductFieldMapper;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.productField.repository.ProductFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFieldService {

    private final ProductFieldMapper productFieldMapper;
    private final ProductFieldRepository repository;

    @Transactional(readOnly = true)
    public ProductFieldDTO getProductFieldById(Long fieldId){
        return repository.findById(fieldId)
                .map(productFieldMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + fieldId));
    }

    @Transactional(readOnly = true)
    public List<ProductFieldDTO> getAllProductField(){
        List<ProductFieldEntity> productEntities = repository.findAll();
        return productEntities.stream()
                .map(productFieldMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductFieldDTO> getProductFieldByProductId(Long productId){
        if(productId == null){
            throw new IllegalArgumentException("productId cannot be null");
        }
        List<ProductFieldEntity> productFieldEntities = repository.findByProductId(productId);
        return productFieldEntities.stream()
                .map(productFieldMapper::toDto)
                .toList();
    }

    @Transactional
    public ProductFieldDTO addProductField(ProductFieldDTO productFieldDTO){
        if(productFieldDTO == null){
            throw new IllegalArgumentException("ProductFieldDTO cannot be null");
        }
        try{
            ProductFieldEntity productFieldEntity = productFieldMapper.toEntity(productFieldDTO);
            ProductFieldEntity savedProductFieldEntity = repository.save(productFieldEntity);
            return productFieldMapper.toDto(savedProductFieldEntity);
        } catch(Exception e){
            throw e;
        }
    }

    @Transactional
    public ProductFieldDTO updateProductField(Long id, ProductFieldDTO productFieldDTO){
        if(id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        if(productFieldDTO == null){
            throw new IllegalArgumentException("ProductFieldDTO cannot be null");
        }
        try{
            ProductFieldEntity productFieldEntity = productFieldMapper.toEntity(productFieldDTO);
            productFieldEntity.setId(id);
            ProductFieldEntity updatedProductFieldEntity = repository.save(productFieldEntity);
            return productFieldMapper.toDto(updatedProductFieldEntity);
        } catch(Exception e){
            throw e;
        }
    }

    @Transactional
    public void deleteProductField(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        try{
            repository.deleteById(id);
        } catch(Exception e){
            throw e;
        }
    }
}
