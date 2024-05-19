package com.codefusion.wasbackend.transaction.service;

import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.base.utils.ProcessUploadFileService;
import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.model.NotificationLevel;
import com.codefusion.wasbackend.notification.service.NotificationService;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.transaction.dto.DailyTransactionTotalDTO;
import com.codefusion.wasbackend.transaction.dto.ReturnTransactionDTO;
import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.helper.TransactionHelper;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.transaction.mapper.TransactionMapper;
import com.codefusion.wasbackend.transaction.repository.TransactionRepository;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionService extends BaseService<TransactionEntity, TransactionDTO, TransactionRepository> {

    private final TransactionMapper transactionMapper;
    private final ProductRepository productRepository;
    private final UserMapper userMapper;
    private final NotificationService notificationService;
    private final ProcessUploadFileService processUploadFileService;

    public TransactionService(TransactionRepository repository, ProductRepository productRepository, UserMapper userMapper,
                              UserRepository userRepository, ResourceFileService resourceFileService, NotificationService notificationService,
                              TransactionMapper transactionMapper,ProcessUploadFileService processUploadFileService) {
        super(repository, userRepository, resourceFileService);
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.userMapper = userMapper;
        this.transactionMapper = transactionMapper;
        this.processUploadFileService = processUploadFileService;
    }

    @Override
    protected TransactionDTO convertToDto(TransactionEntity entity) {
        return transactionMapper.toDto(entity);
    }

    @Override
    protected TransactionEntity convertToEntity(TransactionDTO dto) {
        return transactionMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(TransactionDTO dto, TransactionEntity entity) {
        transactionMapper.partialUpdate(dto,entity);
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return the TransactionDTO object representing the transaction
     * @throws RuntimeException if the transaction is not found
     */
    @Transactional(readOnly = true)
    public ReturnTransactionDTO getTransactionById(Long transactionId) {
        TransactionEntity transactionEntity = repository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transactionEntity.getIsDeleted()) {
            throw new RuntimeException("The requested transaction has been deleted");
        }

        ResourceFileDTO fileDTO = null;
        if (transactionEntity.getResourceFile() != null) {
            try {
                fileDTO = resourceFileService.downloadFile(transactionEntity.getResourceFile().getId());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return TransactionHelper.convertToReturnTransactionDto(transactionEntity, fileDTO);
    }

    @Transactional(readOnly = true)
    public TransactionEntity getTransactionEntityById(Long transactionId){
        TransactionEntity transactionEntity = repository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (transactionEntity.getIsDeleted()) {
            throw new RuntimeException("The requested transaction has been deleted");
        }
        return transactionEntity;
    }


    /**
     * Retrieves all transactions.
     *
     * @return a list of TransactionDTO objects representing all transactions
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions(){
        List<TransactionEntity> transactionEntities = repository.findAllByIsDeletedFalse();
        return transactionEntities.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    /**
     * Retrieves all transactions associated with a specific store ID.
     *
     * @param storeId the ID of the store
     * @return a list of TransactionDTO objects representing the transactions
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByProductId(Long storeId) {
        List<TransactionEntity> transactionEntities = repository.findByProductId(storeId);
        return transactionEntities.stream()
                .map(transactionMapper::toDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<DailyTransactionTotalDTO> getDailyTransactionTotalsByStoreId(Long storeId) {
        return repository.findDailyTransactionTotalsByStoreId(storeId);
    }
    /**
     * Adds a new transaction.
     *
     * @param transactionDTO the data transfer object representing the transaction
     * @param file the file associated with the transaction
     * @return the data transfer object representing the added transaction
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public TransactionDTO addTransaction(TransactionDTO transactionDTO, MultipartFile file) throws IOException {
        TransactionEntity transactionEntity = instantiateFileEntity(transactionDTO);
        processUploadFileService.processUpload(file,transactionEntity);

        return transactionMapper.toDto(transactionEntity);
    }


    /**
     * Deletes a transaction entity with the given transaction ID.
     *
     * @param transactionId the ID of the transaction entity to delete
     * @throws IOException              if there is an error with the file operation
     * @throws NullPointerException    if the transaction ID is null
     */
    @Transactional
    public void delete(Long transactionId) throws IOException {
        super.delete(transactionId);
    }


    /**
     * Creates a new TransactionEntity object based on the given TransactionDTO object.
     *
     * @param transactionDTO the TransactionDTO object to create a TransactionEntity from
     * @return the created TransactionEntity object
     */
    private TransactionEntity instantiateFileEntity(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = transactionMapper.toEntity(transactionDTO);

        ProductEntity productEntity = productRepository.findById(transactionDTO.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + transactionDTO.getProduct().getId()));

        transactionEntity.setProduct(productEntity);

        List<UserEntity> userEntityList = transactionEntity.getProduct().getStore().getUser();

        for(UserEntity userEntity : userEntityList){

            UserDTO user = userMapper.toDto(userEntity);
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setSubject("New Transaction");
            notificationDTO.setText("New transaction occurred");
            String description = String.format("Transaction details: Product - %s, Store - %s",
                    transactionEntity.getProduct().getName(), transactionEntity.getProduct().getStore().getName());
            if (user.getTelegramId() != null) {
                notificationDTO.setTelegramId(user.getTelegramId());
            }
            if (user.getIsTelegram() != null) {
                notificationDTO.setIsTelegram(user.getIsTelegram());
            }
            notificationDTO.setDescription(description);
            notificationDTO.setIsDeleted(false);
            notificationDTO.setUser(userMapper.toDto(user));
            notificationDTO.setNotificationLevel(Collections.singleton(NotificationLevel.SUCCESS));

            notificationService.createNotification(notificationDTO);
        }

        repository.save(transactionEntity);

        return transactionEntity;
    }

}
