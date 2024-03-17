package com.codefusion.wasbackend.Store.Repository;

import com.codefusion.wasbackend.Store.Model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
