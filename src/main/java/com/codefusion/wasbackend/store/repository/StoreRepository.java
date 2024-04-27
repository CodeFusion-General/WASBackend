package com.codefusion.wasbackend.store.repository;

import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    /**
     * Finds stores based on the user ID.
     *
     * @param userId the ID of the user
     * @return a list of StoreEntity objects associated with the specified user ID
     */
    @Query("SELECT s from StoreEntity s JOIN s.user u where u.id = :userId")
    List<StoreEntity> findByUserId (@Param("userId") Long userId);

    @Query("SELECT s from StoreEntity s where s.isDeleted = false")
    List<StoreEntity> findAllByIsDeletedFalse();

    @Query("SELECT s from StoreEntity s where s.id = :storeId and s.isDeleted = false")
    Optional<StoreEntity> findById(@Param("storeId") Long storeId);

    @Query("SELECT s from StoreEntity s JOIN s.user u where u.id = :userId and s.isDeleted = false")
    List<StoreEntity> findByUserIdAndIsDeletedFalse(@Param("userId") Long userId);

}
