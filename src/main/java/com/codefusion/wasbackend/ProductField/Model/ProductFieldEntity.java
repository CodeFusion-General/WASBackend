package com.codefusion.wasbackend.ProductField.Model;

import com.codefusion.wasbackend.Product.Model.ProductEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "product_field")
public class ProductFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Feature cannot be null")
    @Column(name = "feature")
    private String feature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

}