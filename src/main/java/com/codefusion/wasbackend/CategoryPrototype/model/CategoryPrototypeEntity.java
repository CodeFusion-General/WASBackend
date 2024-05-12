package com.codefusion.wasbackend.CategoryPrototype.model;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
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
@Table(name = "category_prototype")
public class CategoryPrototypeEntity {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;
}
