package com.codefusion.wasbackend.product.model;

import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    @NotBlank(message = "Name cannot be null")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Model cannot be null")
    @Column(name = "model")
    private String model;

    @NotBlank(message = "Category cannot be empty")
    @Column(name = "category")
    private String category;

    @NotEmpty(message = "Quantity cannot be null")
    @Column(name = "quantity")
    private int quantity;

    @NotEmpty(message = "Profit cannot be null")
    @Column(name = "profit")
    private double profit;

    @NotEmpty(message = "Product code cannot be null")
    @Column(name = "product_code")
    private String productCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFieldEntity> productFields;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions;

}
