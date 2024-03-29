package com.codefusion.wasbackend.productField.repository;

import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFieldRepository extends JpaRepository<ProductFieldEntity, Long> {

    @Query("SELECT p from ProductFieldEntity p WHERE p.product.id = :productId")
    List<ProductFieldEntity> findByProductId (@Param("productId") Long productId);
}
