package com.codefusion.wasbackend.base.service;

import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public abstract class BaseService<T extends BaseEntity, D, R extends JpaRepository<T, Long>> {

    protected final R repository;
    protected final ResourceFileService resourceFileService;
    protected final StoreRepository storeRepository;
    private final UserRepository userRepository;

    private enum ProcessType {
        ADD, DELETE, UPDATE
    }

    public BaseService(R repository, UserRepository userRepository, ResourceFileService resourceFileService, StoreRepository storeRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.resourceFileService = resourceFileService;
        this.storeRepository = storeRepository;
    }

    protected abstract D convertToDto(T entity);
    protected abstract T convertToEntity(D dto);
    protected abstract void updateEntity(D dto, T entity);

    @Transactional
    public D add(D dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(dto, "DTO cannot be null");
        T newEntity = convertToEntity(dto);

        // Check if the new entity is a UserEntity
        if (newEntity instanceof UserEntity) {
            UserEntity userEntity = (UserEntity) newEntity;

            // Check if the DTO has storeIds
            if (dto instanceof UserDTO) {
                UserDTO userDTO = (UserDTO) dto;
                List<Long> storeIds = userDTO.getStoreIds();

                if (storeIds != null && !storeIds.isEmpty()) {
                    List<StoreEntity> stores = storeRepository.findAllById(storeIds);
                    userEntity.setStores(stores); // Assuming UserEntity has a setStores method
                }
            }
        }

        newEntity.setIsDeleted(false);
        newEntity = repository.save(newEntity);
        handleFile(newEntity, file, ProcessType.ADD);
        return convertToDto(newEntity);
    }


    @Transactional
    public D update(Long entityId, D dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(entityId, "Entity ID cannot be null");
        Objects.requireNonNull(dto, "DTO cannot be null");

        T existingEntity = repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));
        updateEntity(dto, existingEntity);
        handleFile(existingEntity, file, ProcessType.UPDATE);
        repository.flush();
        T updatedEntity = repository.save(existingEntity);
        return convertToDto(updatedEntity);
    }

    @Transactional
    public void delete(Long entityId) throws IOException {
        Objects.requireNonNull(entityId, "Entity ID cannot be null");

        T existingEntity = repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));

        handleFile(existingEntity, null, ProcessType.DELETE);

        existingEntity.setIsDeleted(true);

        repository.save(existingEntity);
    }

    private void handleFile(T existingEntity, MultipartFile file, ProcessType processType) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (processType == ProcessType.UPDATE && existingEntity.getResourceFile() != null) {
                Long oldFileId = existingEntity.getResourceFile().getId();
                resourceFileService.updateFile(oldFileId, file);
            } else if (processType == ProcessType.ADD) {
                resourceFileService.saveFile(file, existingEntity);
            } else {
                throw new IllegalArgumentException("Invalid process type");
            }
        } else {
            if (processType == ProcessType.DELETE && existingEntity.getResourceFile() != null) {
                Long oldFileId = existingEntity.getResourceFile().getId();
                resourceFileService.deleteFile(oldFileId);
            }
        }
    }
}
