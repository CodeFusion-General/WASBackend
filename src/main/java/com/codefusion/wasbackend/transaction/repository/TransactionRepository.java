package com.codefusion.wasbackend.transaction.repository;

import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.product.id = :productId")
    List<TransactionEntity> findByProductId (@Param("productId") Long productId);
}
