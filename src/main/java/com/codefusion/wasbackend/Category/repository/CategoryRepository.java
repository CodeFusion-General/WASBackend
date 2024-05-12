package com.codefusion.wasbackend.Category.repository;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c from CategoryEntity c where c.isDelete = false")
    List<CategoryEntity> findAllByIsDeletedFalse();
}
