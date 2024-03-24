package com.codefusion.wasbackend.product.repository;

import com.codefusion.wasbackend.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p from ProductEntity p WHERE p.store.id = :storeId")
    List<ProductEntity> findByStoreId (@Param("storeId") Long storeId);

}
