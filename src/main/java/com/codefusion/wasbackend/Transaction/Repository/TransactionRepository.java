package com.codefusion.wasbackend.Transaction.Repository;

import com.codefusion.wasbackend.Transaction.Model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
