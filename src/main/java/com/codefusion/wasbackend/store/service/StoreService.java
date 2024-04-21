package com.codefusion.wasbackend.store.service;


import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.mapper.StoreMapper;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StoreService extends BaseService<StoreEntity, StoreDTO, StoreRepository> {

    private final StoreMapper storeMapper;
    public StoreService(StoreRepository repository, ResourceFileService resourceFileService, StoreMapper storeMapper) {
        super(repository, resourceFileService);
        this.storeMapper = storeMapper;
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
     * @param id the ID of the store
     * @return the StoreDTO object representing the retrieved store
     * @throws RuntimeException if the store is not found
     */
    @Transactional(readOnly = true)
    public StoreDTO getStoreById(Long id) {
        return storeMapper.toDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Store not found")));
    }

    /**
     * Retrieves a list of all stores.
     *
     * @return a list of StoreDTO representing all stores
     */
    @Transactional(readOnly = true)
    public List<StoreDTO> getAllStores(){
        List<StoreEntity> userEntities = repository.findAll();
        return userEntities.stream()
                .map(storeMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a list of stores based on the user ID.
     *
     * @param userId the ID of the user
     * @return a list of StoreDTO objects representing the stores associated with the specified user ID
     */
    @Transactional(readOnly = true)
    public List<StoreDTO> getStoresByUserId(Long useId) {
        List<StoreEntity> userEntities = repository.findByUserId(useId);
        return userEntities.stream()
                .map(storeMapper::toDto)
                .toList();
    }

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
        return super.add(storeDTO, file);
    }

    /**
     * Deletes a store with the specified store ID.
     *
     * @param storeId the ID of the store to delete
     * @throws IOException if there is an error with the file operation
     * @throws EntityNotFoundException if the store is not found
     * @throws NullPointerException if the store ID is null
     */
    @Transactional
    public void delete(Long storeId) throws IOException {
        super.delete(storeId);
    }

}
