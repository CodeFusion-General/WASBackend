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


    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long productId){
        return productMapper.toDto(repository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found")));
    }

    //get all products
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts(){
        List<ProductEntity> productEntities = repository.findAll();
        return productEntities.stream()
                .map(productMapper::toDto)
                .toList();
    }

    //get products by store id
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByStoreId(Long storeId) {
        List<ProductEntity> productEntities = repository.findByStoreId(storeId);
        return productEntities.stream()
                .map(productMapper::toDto)
                .toList();
    }

    //add Product
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile file) throws IOException {
        return super.add(productDTO, file);
    }


    //Delete Product
    @Transactional
    public void delete(Long productId) throws IOException {
        super.delete(productId);
    }
    
}
