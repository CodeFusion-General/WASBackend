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

    //get user by id
    @Transactional(readOnly = true)
    public StoreDTO getStoreById(Long id) {
        return storeMapper.toDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Store not found")));
    }

    //get all users
    @Transactional(readOnly = true)
    public List<StoreDTO> getAllStores(){
        List<StoreEntity> userEntities = repository.findAll();
        return userEntities.stream()
                .map(storeMapper::toDto)
                .toList();
    }

    //get users by store id
    @Transactional(readOnly = true)
    public List<StoreDTO> getStoresByUserId(Long useId) {
        List<StoreEntity> userEntities = repository.findByUserId(useId);
        return userEntities.stream()
                .map(storeMapper::toDto)
                .toList();
    }

    @Transactional
    public StoreDTO addStore(StoreDTO storeDTO, MultipartFile file) throws IOException {
        return super.add(storeDTO, file);
    }

    @Transactional
    public void delete(Long storeId) throws IOException {
        super.delete(storeId);
    }

}
