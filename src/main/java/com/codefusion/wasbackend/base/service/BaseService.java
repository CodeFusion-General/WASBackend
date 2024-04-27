package com.codefusion.wasbackend.base.service;
import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.model.StoreEntity;
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
    private final UserRepository userRepository;
    private enum ProcessType {
        ADD, DELETE, UPDATE
    }



    public BaseService(R repository, UserRepository userRepository, ResourceFileService resourceFileService) {
        this.repository = repository;
        this.userRepository=userRepository;
        this.resourceFileService = resourceFileService;
    }

    protected abstract D convertToDto(T entity);
    protected abstract T convertToEntity(D dto);
    protected abstract void updateEntity(D dto, T entity);



    /**
     * Adds a new entity.
     *
     * @param dto the data transfer object representing the entity
     * @param file the file associated with the entity
     * @return the data transfer object representing the added entity
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the dto is null
     */
    @Transactional
    public D add(D dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(dto, "DTO cannot be null");
        T newEntity = convertToEntity(dto);

        if(newEntity instanceof StoreEntity) {
            List<UserEntity> users = userRepository.findByIdIn(((StoreDTO) dto).getUserIds());
            ((StoreEntity) newEntity).setUser(users);
        }

        newEntity = repository.save(newEntity);
        handleFile(newEntity, file, ProcessType.ADD);
        return convertToDto(newEntity);
    }

    /**
     * Updates an entity with the given entity ID and DTO, optionally including a file.
     *
     * @param entityId the ID of the entity to update
     * @param dto the data transfer object representing the updated entity
     * @param file the file associated with the entity
     * @return the data transfer object representing the updated entity
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the entity ID or DTO is null
     */
    @Transactional
    public D update(Long entityId, D dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(entityId, "Entity ID cannot be null");
        Objects.requireNonNull(dto, "DTO cannot be null");

        T existingEntity = repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));
        updateEntity(dto, existingEntity);
        handleFile(existingEntity, file,ProcessType.UPDATE);
        repository.flush();
        T updatedEntity = repository.save(existingEntity);
        return convertToDto(updatedEntity);
    }

    /**
     * Deletes an entity with the given entity ID.
     *
     * @param entityId the ID of the entity to delete
     * @throws IOException if there is an error with the file operation
     * @throws EntityNotFoundException if the entity is not found
     * @throws NullPointerException if the entity ID is null
     */
    @Transactional
    public void delete(Long entityId) throws IOException {
        Objects.requireNonNull(entityId, "Entity ID cannot be null");

        T existingEntity = repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));

        handleFile(existingEntity, null, ProcessType.DELETE);

        existingEntity.setIsDeleted(true);
        repository.save(existingEntity);
    }


    /**
     * Handles the file associated with an entity based on the process type.
     *
     * @param existingEntity the existing entity
     * @param file the file associated with the entity
     * @param processType the process type indicating how to handle the file
     * @throws IOException if there is an error with the file operation
     */
    private void handleFile(T existingEntity, MultipartFile file, ProcessType processType) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (processType == ProcessType.UPDATE && existingEntity.getResourceFile() != null) {
                Long oldFileId = existingEntity.getResourceFile().getId();
                resourceFileService.updateFile(oldFileId, file);
            } else if(processType == ProcessType.ADD) {
                resourceFileService.saveFile(file, existingEntity);
            }
            else {
                throw new IllegalArgumentException("Invalid process type");
            }
        }
        else {
            if(processType == ProcessType.DELETE && existingEntity.getResourceFile() != null){
                Long oldFileId = existingEntity.getResourceFile().getId();
                resourceFileService.deleteFile(oldFileId);
            }
        }
    }
}