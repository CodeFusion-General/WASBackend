package com.codefusion.wasbackend.Transaction.Repository;

import com.codefusion.wasbackend.Transaction.Model.TransactionEntity;
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
