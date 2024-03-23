package com.codefusion.wasbackend.store.repository;

import com.codefusion.wasbackend.store.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    @Query("SELECT s from StoreEntity s JOIN s.user u where u.id = :userId")
    List<StoreEntity> findByUserId (@Param("userId") Long userId);
}
