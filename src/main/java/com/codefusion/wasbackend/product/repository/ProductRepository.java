package com.codefusion.wasbackend.product.repository;

import com.codefusion.wasbackend.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    /**
     *
     * Retrieves a list of products based on the store ID.
     *
     * @param storeId the ID of the store
     * @return the list of ProductEntity objects corresponding to the products of the store
     */
    @Query("SELECT p FROM ProductEntity p WHERE p.store.id = :storeId AND p.isDeleted = false")
    List<ProductEntity> findByStoreId (@Param("storeId") Long storeId);


    @Query("SELECT p FROM ProductEntity p WHERE p.isDeleted = false")
    List<ProductEntity> findAllByIsDeletedFalse();

}
