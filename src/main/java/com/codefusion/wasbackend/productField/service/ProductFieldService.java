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

    /**
     * Retrieves the {@link ProductFieldDTO} associated with the given fieldId.
     *
     * @param fieldId the ID of the product field to retrieve
     * @return the {@link ProductFieldDTO} associated with the given fieldId
     * @throws IllegalArgumentException if no product field is found with the given fieldId
     */
    @Transactional(readOnly = true)
    public ProductFieldDTO getProductFieldById(Long fieldId){
        return repository.findById(fieldId)
                .map(productFieldMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + fieldId));
    }

    /**
     * Retrieves all product fields.
     *
     * @return a list of {@link ProductFieldDTO} representing all product fields.
     */
    @Transactional(readOnly = true)
    public List<ProductFieldDTO> getAllProductField(){
        List<ProductFieldEntity> productEntities = repository.findAll();
        return productEntities.stream()
                .map(productFieldMapper::toDto)
                .toList();
    }

    /**
     * Retrieves the list of {@link ProductFieldDTO} objects associated with a given productId.
     *
     * @param productId the ID of the product
     * @return the list of ProductFieldDTO objects associated with the given productId
     * @throws IllegalArgumentException if productId is null
     */
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

    /**
     * Adds a new product field.
     *
     * @param productFieldDTO the DTO representing the product field to be added (must not be null)
     * @return the DTO representing the added product field
     * @throws IllegalArgumentException if the productFieldDTO is null
     */
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

    /**
     * Updates a product field with the specified ID.
     *
     * @param id The ID of the product field to update.
     * @param productFieldDTO The updated product field data.
     * @return The updated product field.
     * @throws IllegalArgumentException if the ID or product field DTO is null.
     */
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

    /**
     * Deletes a product field by its ID.
     *
     * @param id the ID of the product field to be deleted
     * @throws IllegalArgumentException if the ID is null
     * @throws Exception if an error occurs while deleting the product field
     */
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
