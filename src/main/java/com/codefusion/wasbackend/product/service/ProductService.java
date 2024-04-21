package com.codefusion.wasbackend.product.service;


import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService extends BaseService<ProductEntity, ProductDTO, ProductRepository> {

    private final ProductMapper productMapper;

    public ProductService(ProductRepository repository, ResourceFileService resourceFileService, ProductMapper productMapper) {
        super(repository, resourceFileService);
        this.productMapper = productMapper;
    }

    @Override
    protected ProductDTO convertToDto(ProductEntity entity) {
        return productMapper.toDto(entity);
    }

    @Override
    protected ProductEntity convertToEntity(ProductDTO dto) {
        return productMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(ProductDTO dto, ProductEntity entity) {
        productMapper.partialUpdate(dto,entity);
    }


    /**
     * Retrieves a {@link ProductDTO} by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the {@link ProductDTO} corresponding to the ID
     * @throws RuntimeException if the product is not found
     */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long productId){
        return productMapper.toDto(repository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found")));
    }

    /**
     * Retrieves all products.
     *
     * @return a list of {@link ProductDTO} objects representing the products.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts(){
        List<ProductEntity> productEntities = repository.findAll();
        return productEntities.stream()
                .map(productMapper::toDto)
                .toList();
    }

    /**
     * Get the list of products by store ID.
     *
     * @param storeId the ID of the store
     * @return the list of ProductDTO objects corresponding to the products of the store
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByStoreId(Long storeId) {
        List<ProductEntity> productEntities = repository.findByStoreId(storeId);
        return productEntities.stream()
                .map(productMapper::toDto)
                .toList();
    }

    /**
     * Adds a new product.
     *
     * @param productDTO the data transfer object representing the product
     * @param file the file associated with the product
     * @return the data transfer object representing the added product
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile file) throws IOException {
        return super.add(productDTO, file);
    }


    /**
     * Deletes a product with the given product ID.
     *
     * @param productId the ID of the product to delete
     * @throws IOException if there is an error with the file operation
     * @throws EntityNotFoundException if the product is not found
     * @throws NullPointerException if the product ID is null
     */
    @Transactional
    public void delete(Long productId) throws IOException {
        super.delete(productId);
    }
    
}
