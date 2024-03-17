package com.codefusion.wasbackend.Product.Model;

import com.codefusion.wasbackend.ProductField.Model.ProductFieldEntity;
import com.codefusion.wasbackend.Store.Model.StoreEntity;
import com.codefusion.wasbackend.Transaction.Model.TransactionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null")
    @Column(name = "name")
    private String name;

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
    private Collection<ProductFieldEntity> productFields;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<TransactionEntity> transactions;

}
