package com.codefusion.wasbackend.Category.repository;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
