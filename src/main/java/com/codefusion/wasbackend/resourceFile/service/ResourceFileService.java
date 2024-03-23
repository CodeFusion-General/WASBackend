package com.codefusion.wasbackend.resourceFile.service;


import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.model.ResourceFileEntity;
import com.codefusion.wasbackend.resourceFile.repository.ResourceFileRepository;
import com.codefusion.wasbackend.resourceFile.utility.ResourceFileUtils;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class ResourceFileService {

    private final ResourceFileRepository fileRepository;

    private static final String ENTITY_CANNOT_BE_NULL = "Entity cannot be null";
    private static final String FILE_EMPTY = "File is empty";
    private static final String FILE_NOT_FOUND_MSG = "File not found with id: ";
    private static final String DELETION_ERROR_MSG = "Error occurred while deleting the file with id: ";
    public static final String UPDATED_MSG = "Updated file in DB with name: %s";

    @Transactional
    public String saveFile(MultipartFile file, BaseEntity entity) throws IOException {
        validateFileAndEntity(file, entity);

        ResourceFileEntity fileEntity = createResourceFileEntity(file);
        associateEntityWithFile(entity, fileEntity);
        fileRepository.save(fileEntity);
        return "Saved file in DB with name: " + fileEntity.getName();
    }

    private void validateFileAndEntity(MultipartFile file, BaseEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(ENTITY_CANNOT_BE_NULL);
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException(FILE_EMPTY);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException(FILE_EMPTY);
        }
    }

    private ResourceFileEntity createResourceFileEntity(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String contentType = file.getContentType();
        byte[] compressedData = ResourceFileUtils.compressBytes(file.getBytes());

        return ResourceFileEntity.builder()
                .name(fileName)
                .type(contentType)
                .data(compressedData)
                .build();
    }

    @Transactional(readOnly = true)
    public String getFileName(Long fileId) throws FileNotFoundException {
        return fileRepository.findById(fileId)
                .map(ResourceFileEntity::getName)
                .orElseThrow(() -> new FileNotFoundException(FILE_NOT_FOUND_MSG + fileId));
    }

    @Transactional
    public String updateFile(Long id, MultipartFile file) throws IOException {
        validateFile(file);
        ResourceFileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(String.format(FILE_NOT_FOUND_MSG, id)));
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileEntity.setName(fileName);
        fileEntity.setType(file.getContentType());
        fileEntity.setData(ResourceFileUtils.compressBytes(file.getBytes()));
        fileRepository.save(fileEntity);
        return String.format(UPDATED_MSG, fileName);
    }

    @Transactional(readOnly = true)
    public ResourceFileDTO downloadFile(Long fileId) throws FileNotFoundException {
        ResourceFileEntity retrievedFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(FILE_NOT_FOUND_MSG + fileId));
        byte[] decompressedData = ResourceFileUtils.decompressBytes(retrievedFile.getData());
        return new ResourceFileDTO(decompressedData, retrievedFile.getName());
    }

    public void associateEntityWithFile(BaseEntity entity, ResourceFileEntity fileEntity) {
        String entityClassName = entity.getClass().getSimpleName();

        switch (entityClassName) {
            case "ProductEntity":
                ((ProductEntity) entity).setResourceFile(fileEntity);
                break;
            case "UserEntity":
                ((UserEntity) entity).setResourceFile(fileEntity);
                break;
            case "StoreEntity":
                ((StoreEntity) entity).setResourceFile(fileEntity);
                break;
            case "TransactionEntity":
                ((TransactionEntity) entity).setResourceFile(fileEntity);
                break;
            default:
                throw new IllegalArgumentException("Entity type not supported");
        }
    }

    @Transactional
    public void deleteFile(Long fileId) throws FileNotFoundException {
        ResourceFileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(FILE_NOT_FOUND_MSG + fileId));
        try {
            fileRepository.delete(file);
        } catch (Exception e) {
            throw new ServiceException(DELETION_ERROR_MSG + fileId, e);
        }
    }
}