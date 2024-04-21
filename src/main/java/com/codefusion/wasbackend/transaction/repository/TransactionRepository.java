package com.codefusion.wasbackend.transaction.repository;

import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * Retrieves all transactions with a specific product ID.
     *
     * @param productId the ID of the product
     * @return a list of TransactionEntity objects representing the transactions
     */
    @Query("SELECT t FROM TransactionEntity t WHERE t.product.id = :productId")
    List<TransactionEntity> findByProductId (@Param("productId") Long productId);
}
