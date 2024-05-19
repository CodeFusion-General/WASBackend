package com.codefusion.wasbackend.transaction.helper;

import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.transaction.dto.ReturnTransactionDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;

public class TransactionHelper {

    public static ReturnTransactionDTO convertToReturnTransactionDto(TransactionEntity transactionEntity, ResourceFileDTO fileDTO) {
        ReturnTransactionDTO.ResourceFileDto resourceFileDto = null;
        if (fileDTO != null) {
            resourceFileDto = mapResourceFile(fileDTO);
        }

        return ReturnTransactionDTO.builder()
                .id(transactionEntity.getId())
                .isDeleted(transactionEntity.getIsDeleted())
                .isBuying(transactionEntity.getIsBuying())
                .date(transactionEntity.getDate())
                .price(transactionEntity.getPrice())
                .fullName(transactionEntity.getFullName())
                .quantity(transactionEntity.getQuantity())
                .address(transactionEntity.getAddress())
                .phone(transactionEntity.getPhone())
                .product(transactionEntity.getProduct() != null ? mapProduct(transactionEntity.getProduct()) : null)
                .resourceFile(resourceFileDto)
                .build();
    }

    public static ReturnTransactionDTO.ResourceFileDto mapResourceFile(ResourceFileDTO fileDTO) {
        return ReturnTransactionDTO.ResourceFileDto.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getFileName())
                .type(fileDTO.getContentType())
                .data(fileDTO.getData())
                .build();
    }

    private static ReturnTransactionDTO.ProductDTO mapProduct(ProductEntity productEntity) {
        return ReturnTransactionDTO.ProductDTO.builder()
                .id(productEntity.getId())
                .isDeleted(productEntity.getIsDeleted())
                .name(productEntity.getName())
                .model(productEntity.getModel())
                .currentStock(productEntity.getCurrentStock())
                .profit(productEntity.getProfit())
                .productCode(productEntity.getProductCode())
                .build();
    }
}
