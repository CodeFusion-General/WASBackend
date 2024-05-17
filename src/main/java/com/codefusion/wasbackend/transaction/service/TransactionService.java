package com.codefusion.wasbackend.transaction.service;

import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.base.utils.ProcessUploadFileService;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.mapper.ResourceFileMapper;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import com.codefusion.wasbackend.transaction.dto.ReturnTransactionDTO;
import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.transaction.mapper.TransactionMapper;
import com.codefusion.wasbackend.transaction.repository.TransactionRepository;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class TransactionService extends BaseService<TransactionEntity, TransactionDTO, TransactionRepository> {

    private final TransactionMapper transactionMapper;
    private final ProductRepository productRepository;
    private final ProcessUploadFileService processUploadFileService;
    private final ResourceFileMapper resourceFileMapper;

    public TransactionService(TransactionRepository repository, ProductRepository productRepository,
                              UserRepository userRepository, ResourceFileService resourceFileService,
                              TransactionMapper transactionMapper, ResourceFileMapper resourceFileMapper,
                              ProcessUploadFileService processUploadFileService) {
        super(repository, userRepository, resourceFileService);
        this.productRepository = productRepository;
        this.resourceFileMapper = resourceFileMapper;
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
    public ReturnTransactionDTO getTransactionById(Long transactionId){
        TransactionEntity transactionEntity = repository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transactionEntity.getIsDeleted()) {
            throw new RuntimeException("The requested transaction has been deleted");
        }

        ReturnTransactionDTO.ResourceFileDto resourceFileDto = null;
        if (transactionEntity.getResourceFile() != null) {
            try {
                ResourceFileDTO fileDTO = resourceFileService.downloadFile(transactionEntity.getResourceFile().getId());
                resourceFileDto = mapResourceFile(fileDTO);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        ReturnTransactionDTO transactionDto = transactionMapper.toReturnDto(transactionEntity);
        return ReturnTransactionDTO.builder()
                .id(transactionDto.getId())
                .isDeleted(transactionDto.getIsDeleted())
                .isBuying(transactionDto.getIsBuying())
                .date(transactionDto.getDate())
                .price(transactionDto.getPrice())
                .fullName(transactionDto.getFullName())
                .quantity(transactionDto.getQuantity())
                .address(transactionDto.getAddress())
                .phone(transactionDto.getPhone())
                .product(transactionDto.getProduct())
                .resourceFile(resourceFileDto)
                .build();
    }

    public ReturnTransactionDTO.ResourceFileDto mapResourceFile(ResourceFileDTO fileDTO){
        return ReturnTransactionDTO.ResourceFileDto.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getFileName())
                .type(fileDTO.getContentType())
                .data(fileDTO.getData())
                .build();
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
        repository.save(transactionEntity);

        return transactionEntity;
    }

}
