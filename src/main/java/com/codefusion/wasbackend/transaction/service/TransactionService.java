package com.codefusion.wasbackend.transaction.service;

import com.codefusion.wasbackend.base.service.BaseService;
import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.transaction.mapper.TransactionMapper;
import com.codefusion.wasbackend.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TransactionService extends BaseService<TransactionEntity, TransactionDTO, TransactionRepository> {

    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository repository, ResourceFileService resourceFileService, TransactionMapper transactionMapper) {
        super(repository, resourceFileService);
        this.transactionMapper = transactionMapper;
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

    //get transaction
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(Long transactionId){
        return transactionMapper.toDto(repository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found")));
    }

    //get all transactions
    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions(){
        List<TransactionEntity> transactionEntities = repository.findAll();
        return transactionEntities.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    //get transactions by store id
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByStoreId(Long productId) {
        List<TransactionEntity> transactionEntities = repository.findByProductId(productId);
        return transactionEntities.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    //add Transaction
    @Transactional
    public TransactionDTO addTransaction(TransactionDTO transactionDTO, MultipartFile file) throws IOException {
        return super.add(transactionDTO, file);
    }


    //Delete Transaction
    @Transactional
    public void delete(Long transactionId) throws IOException {
        super.delete(transactionId);
    }

}
