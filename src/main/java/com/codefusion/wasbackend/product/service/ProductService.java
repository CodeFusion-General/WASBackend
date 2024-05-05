package com.codefusion.wasbackend.product.service;



import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.base.utils.ProcessUploadFileService;
import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.service.NotificationService;
import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.mapper.StoreMapper;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class ProductService extends BaseService<ProductEntity, ProductDTO, ProductRepository> {

    private final ProductMapper productMapper;
    private final ProcessUploadFileService processUploadFileService;
    private final StoreRepository storeRepository;
    private final NotificationService notificationService;
    private final StoreMapper storeMapper;
    private final UserMapper userMapper;

    public ProductService(ProductRepository repository, UserRepository userRepository,
                          ResourceFileService resourceFileService, ProductMapper productMapper,
                          ProcessUploadFileService processUploadFileService, NotificationService notificationService,
                          StoreRepository storeRepository, StoreMapper storeMapper, UserMapper userMapper) {
        super(repository, userRepository, resourceFileService);
        this.productMapper = productMapper;
        this.processUploadFileService = processUploadFileService;
        this.storeRepository = storeRepository;
        this.notificationService = notificationService;
        this.storeMapper = storeMapper;
        this.userMapper = userMapper;
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
    public ProductDTO getProductById(Long productId) {
        ProductEntity product = repository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if(product.getIsDeleted()){
            throw new RuntimeException("The requested product has been deleted");
        }
        return productMapper.toDto(product);
    }

    /**
     * Retrieves a {@link ProductEntity} by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the {@link ProductEntity} corresponding to the ID
     * @throws RuntimeException if the product is not found or has been deleted
     */
    @Transactional(readOnly = true)
    public ProductEntity getProductEntityById(Long productId) {
        ProductEntity product = repository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if(product.getIsDeleted()){
            throw new RuntimeException("The requested product has been deleted");
        }
        return product;
    }

    /**
     * Retrieves all products.
     *
     * @return a list of {@link ProductDTO} objects representing the products.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntities = repository.findAllByIsDeletedFalse();
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
     * Adds a {@link ProductDTO} to the system and sends a notification to all users of the associated store.
     *
     * @param productDTO the {@link ProductDTO} object representing the product to be added
     * @param file the file to be processed and stored for the product (optional)
     * @return the {@link ProductDTO} object representing the added product
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile file) throws IOException {
        ProductEntity productEntity = instantiateFileEntity(productDTO);

        processUploadFileService.processUpload(file, productEntity);

        productEntity.getStore().getUser().stream()
                .map(userMapper::toDto)
                .forEach(user -> {
                    NotificationDTO notificationDTO = new NotificationDTO();
                    notificationDTO.setSubject("New Product Addition");
                    notificationDTO.setText("New product added");
                    String description = String.format("Product details: Name - %s, Model - %s, Category - %s, Profit - %s, Current Stock - %s",
                            productEntity.getName(), productEntity.getModel(), productEntity.getCategory(),
                            productEntity.getProfit(), productEntity.getCurrentStock());
                    notificationDTO.setDescription(description);
                    notificationDTO.setStore(productEntity.getStore());
                    notificationDTO.setUser(userMapper.toEntity(user));

                    notificationService.createNotification(notificationDTO);
                });

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
