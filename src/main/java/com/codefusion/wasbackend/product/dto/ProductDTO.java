package com.codefusion.wasbackend.Product.dto;

import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class ProductDTO implements Serializable {
    Long id;
    @NotBlank(message = "Name cannot be null")
    String name;
    @NotBlank(message = "Model cannot be null")
    String model;
    @NotBlank(message = "Category cannot be null")
    String category;
    int currentStock;
    double profit;
    @NotEmpty(message = "Product code cannot be null")
    String productCode;
    StoreEntity store;
    List<ProductFieldEntity> productFields;
    List<TransactionEntity> transactions;
    Long resourceFileId;
}