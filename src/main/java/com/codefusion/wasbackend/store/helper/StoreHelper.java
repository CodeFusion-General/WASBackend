package com.codefusion.wasbackend.store.helper;

import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;

public class StoreHelper {

    public static ReturnStoreDTO.ProductDto convertToProductDto(ProductEntity productEntity) {
        return ReturnStoreDTO.ProductDto.builder()
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