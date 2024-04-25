package com.codefusion.wasbackend.product.service;


import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.base.utils.ProcessUploadFileService;
import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.dto.ProfitAndQuantityDTO;
import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService extends BaseService<ProductEntity, ProductDTO, ProductRepository> {

    private final ProductMapper productMapper;
    private final ProcessUploadFileService processUploadFileService;
    private final StoreRepository storeRepository;

    public ProductService(ProductRepository repository, UserRepository userRepository, ResourceFileService resourceFileService, ProductMapper productMapper, ProcessUploadFileService processUploadFileService, StoreRepository storeRepository) {
        super(repository, userRepository, resourceFileService);
        this.productMapper = productMapper;
        this.processUploadFileService = processUploadFileService;
        this.storeRepository = storeRepository;
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

    @Transactional(readOnly = true)
    public ProfitAndQuantityDTO getProfitAndQuantityByStoreId(Long storeId) {
        List<ProductEntity> productEntities = repository.findByStoreId(storeId);
        return productEntities.stream()
                .reduce(new ProfitAndQuantityDTO(), (acc, product) -> {
                    acc.setTotalProfit(acc.getTotalProfit() + product.getProfit());
                    acc.setTotalQuantity(acc.getTotalQuantity() + product.getQuantity());
                    return acc;
                }, (profitAndQuantity1, profitAndQuantity2) -> {
                    profitAndQuantity1.setTotalProfit(profitAndQuantity1.getTotalProfit() + profitAndQuantity2.getTotalProfit());
                    profitAndQuantity1.setTotalQuantity(profitAndQuantity1.getTotalQuantity() + profitAndQuantity2.getTotalQuantity());
                    return profitAndQuantity1;
                });
    }


    /**
     * Adds a new product to the system.
     *
     * @param productDTO the {@link ProductDTO} object representing the product to be added
     * @param file the {@link MultipartFile} object representing the uploaded file
     * @return the {@link ProductDTO} object representing the added product
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile file) throws IOException {
        ProductEntity productEntity = instantiateFileEntity(productDTO);

        processUploadFileService.processUpload(file, productEntity);

        return productMapper.toDto(productEntity);
    }


    /**
     * Deletes a product with the given product ID.
     *
     * @param productId the ID of the product to delete
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the product ID is null
     */
    @Transactional
    public void delete(Long productId) throws IOException {
        super.delete(productId);
    }

    /**
     * Instantiate a {@link ProductEntity} object from a {@link ProductDTO} object.
     *
     * @param productDTO the {@link ProductDTO} object representing the product data transfer object to be instantiated
     * @return the instantiated {@link ProductEntity} object
     * @throws IllegalArgumentException if the store with the provided ID is not found
     */
    private ProductEntity instantiateFileEntity(ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.toEntity(productDTO);

        StoreEntity storeEntity = storeRepository.findById(productDTO.getStore().getId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + productEntity.getStore().getId()));

        productEntity.setStore(storeEntity);

        repository.save(productEntity);
        return productEntity;
    }

}
