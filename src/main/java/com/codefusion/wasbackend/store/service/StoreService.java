package com.codefusion.wasbackend.store.service;


import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.mapper.ResourceFileMapper;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.helper.StoreHelper;
import com.codefusion.wasbackend.store.mapper.StoreMapper;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService extends BaseService<StoreEntity, StoreDTO, StoreRepository> {

    private final StoreMapper storeMapper;
    private final UserRepository userRepository;
    private final ResourceFileMapper resourceFileMapper;
    public StoreService(StoreRepository repository, UserRepository userRepository, ResourceFileService resourceFileService,
                        StoreMapper storeMapper, ResourceFileMapper resourceFileMapper) {
        super(repository, userRepository, resourceFileService);
        this.resourceFileMapper = resourceFileMapper;
        this.storeMapper = storeMapper;
        this.userRepository = userRepository;
    }

    @Override
    protected StoreDTO convertToDto(StoreEntity entity) {
        return storeMapper.toDto(entity);
    }

    @Override
    protected StoreEntity convertToEntity(StoreDTO dto) {
        return storeMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(StoreDTO dto, StoreEntity entity) {
        storeMapper.partialUpdate(dto,entity);
    }

    /**
     * Retrieves a StoreDTO object by its ID.
     *
     * @param storeId the ID of the store
     * @return the StoreDTO object representing the retrieved store
     * @throws RuntimeException if the store is not found
     */
    @Transactional(readOnly = true)
    public StoreDTO getStoreById(Long storeId) {
        return storeMapper.toDto(repository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found")));
    }

    /**
     * Retrieves a StoreEntity object by its ID.
     *
     * @param storeId the ID of the store
     * @return the StoreEntity object representing the retrieved store
     * @throws RuntimeException if the store is not found
     */
    @Transactional(readOnly = true)
    public StoreEntity getStoreEntityById(Long storeId) {
        return repository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
    }

    /**
     * Retrieves a list of all stores.
     *
     * @return a list of StoreDTO representing all stores
     */
    @Transactional(readOnly = true)
    public List<StoreDTO> getAllStores(){
        List<StoreEntity> storeEntities = repository.findAllByIsDeletedFalse();
        return storeEntities.stream()
                .map(storeMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReturnStoreDTO.ProductDto> getTop5MostProfitableProducts(Long storeId) {
        StoreEntity storeEntity = repository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return storeEntity.getProducts().stream()
                .sorted(Comparator.comparingDouble(ProductEntity::getProfit).reversed())
                .limit(5)
                .map(StoreHelper::convertToProductDto)
                .collect(Collectors.toList());
    }



    /**
     * Retrieves a list of stores based on the user ID.
     *
     * @return a list of StoreDTO objects representing the stores associated with the specified user ID
     */
    @Transactional(readOnly = true)
    public List<ReturnStoreDTO> getStoresByUserId(Long userId) {
        List<StoreEntity> storeEntities = repository.findByUserIdAndIsDeletedFalse(userId);
        return storeEntities.stream()
                .map(storeEntity -> {
                    ReturnStoreDTO dto = storeMapper.toReturnDto(storeEntity);
                    ReturnStoreDTO.ResourceFileDto resourceFileDto = null;
                    if (storeEntity.getResourceFile() != null) {
                        try {
                            ResourceFileDTO fileDTO = resourceFileService.downloadFile(storeEntity.getResourceFile().getId());
                            resourceFileDto = resourceFileMapper.toReturnDto(fileDTO);
                        } catch (FileNotFoundException e) {
                            // Log exception and continue
                            e.printStackTrace();
                        }
                    }
                    return ReturnStoreDTO.builder()
                            .id(dto.getId())
                            .isDeleted(dto.getIsDeleted())
                            .name(dto.getName())
                            .description(dto.getDescription())
                            .address(dto.getAddress())
                            .storePhoneNo(dto.getStorePhoneNo())
                            .user(dto.getUser())
                            .products(dto.getProducts())
                            .resourceFile(resourceFileDto)
                            .build();
                }).toList();
    }


//    private StoreResourceDTO toUserStoreDto(StoreEntity storeEntity) {
//        StoreDTO storeDTO = storeMapper.toDto(storeEntity);
//        try {
//            Long fileId = resourceFileService.findResourceFileId(storeEntity);
//            ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
//            return new StoreResourceDTO(storeDTO, fileDto);
//        } catch (FileNotFoundException e) {
//            return new StoreResourceDTO(storeDTO, null);
//        }
//    }

    /**
     * Adds a new store to the system.
     *
     * @param storeDTO the data transfer object representing the store
     * @param file the file associated with the store
     * @return the data transfer object representing the added store
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the storeDTO is null
     */
    @Transactional
    public StoreDTO addStore(StoreDTO storeDTO, MultipartFile file) throws IOException {
        return super.add(instantiateStoreEntity(storeDTO), file);
    }

    /**
     * Deletes a store with the specified store ID.
     *
     * @param storeId the ID of the store to delete
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the store ID is null
     */
    @Transactional
    public void delete(Long storeId) throws IOException {
        super.delete(storeId);
    }

    private StoreDTO instantiateStoreEntity(StoreDTO storeDTO){
        StoreEntity storeEntity = storeMapper.toEntity(storeDTO);
        UserEntity userEntity = userRepository.findById(storeDTO.getUserIds().getFirst())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id:" + storeDTO.getUserIds().getFirst()));

        List<UserEntity> userList = new ArrayList<>();
        userList.add(userEntity);
        storeEntity.setUser(userList);

        return storeMapper.toDto(storeEntity);
    }

}
