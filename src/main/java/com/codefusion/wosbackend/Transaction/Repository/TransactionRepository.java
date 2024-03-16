package com.codefusion.wosbackend.Transaction.Repository;

import com.codefusion.wosbackend.Transaction.Model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
