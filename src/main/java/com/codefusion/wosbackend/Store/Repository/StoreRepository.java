package com.codefusion.wosbackend.Store.Repository;

import com.codefusion.wosbackend.Store.Model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
