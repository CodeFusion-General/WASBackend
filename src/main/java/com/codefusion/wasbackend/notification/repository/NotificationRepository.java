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

    /**
     * Retrieves a list of notifications for a specific user that are not marked as deleted.
     *
     * @param user the UserEntity for which notifications are to be retrieved
     * @param isDeleted the deletion status of the notifications (true for deleted, false for not deleted)
     * @return a List of NotificationEntity objects representing the notifications for the user that match the deletion status
     */
    @Query("SELECT n FROM NotificationEntity n WHERE n.user = :user AND n.isDeleted = :isDeleted")
    List<NotificationEntity> findByUserAndIsDeleted(@Param("user") UserEntity user, @Param("isDeleted") Boolean isDeleted);

    /**
     * Retrieves a list of {@link NotificationEntity} objects for a specific {@link StoreEntity} and {@link Boolean} flag indicating whether the notification is deleted or not.
     *
     * @param store     the {@link StoreEntity} for which notifications are to be retrieved
     * @param isDeleted a {@link Boolean} flag indicating whether the notification is deleted or not
     * @return a List of {@link NotificationEntity} objects for the specified store and isDeleted flag
     */
    @Query("SELECT n FROM NotificationEntity n WHERE n.store = :store AND n.isDeleted = :isDeleted")
    List<NotificationEntity> findByStoreAndIsDeleted(@Param("store") StoreEntity store, @Param("isDeleted") Boolean isDeleted);

    /**
     * Retrieves a list of NotificationEntity objects with the given isDeleted status.
     *
     * @param isDeleted the isDeleted status to filter the notifications
     * @return a list of NotificationEntity objects matching the given isDeleted status
     */
    @Query("SELECT n FROM NotificationEntity n WHERE n.isDeleted = :isDeleted")
    List<NotificationEntity> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

    /**
     * Retrieves a NotificationEntity object by its ID and isDeleted flag.
     *
     * @param id the ID of the notification
     * @param isDeleted the isDeleted flag of the notification
     * @return an Optional containing the NotificationEntity object if found, or an empty Optional if not found
     */
    @Query("SELECT n FROM NotificationEntity n WHERE n.id = :id AND n.isDeleted = :isDeleted")
    Optional<NotificationEntity> findByIdAndIsDeleted(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

}