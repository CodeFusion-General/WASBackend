package com.codefusion.wasbackend.Category.model;

import com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity;
import com.codefusion.wasbackend.Product.model.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class CategoryEntity {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryPrototypeEntity> prototypes;

}
