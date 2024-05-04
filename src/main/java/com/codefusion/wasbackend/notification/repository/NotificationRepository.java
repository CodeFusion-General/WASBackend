package com.codefusion.wasbackend.notification.repository;

import com.codefusion.wasbackend.notification.model.NotificationEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT n FROM NotificationEntity n WHERE n.user = :user AND n.isDeleted = :isDeleted")
    List<NotificationEntity> findByUserAndIsDeleted(@Param("user") UserEntity user, @Param("isDeleted") Boolean isDeleted);

    @Query("SELECT n FROM NotificationEntity n WHERE n.store = :store AND n.isDeleted = :isDeleted")
    List<NotificationEntity> findByStoreAndIsDeleted(@Param("store") StoreEntity store, @Param("isDeleted") Boolean isDeleted);

    @Query("SELECT n FROM NotificationEntity n WHERE n.isDeleted = :isDeleted")
    List<NotificationEntity> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

    @Query("SELECT n FROM NotificationEntity n WHERE n.id = :id AND n.isDeleted = :isDeleted")
    Optional<NotificationEntity> findByIdAndIsDeleted(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

}