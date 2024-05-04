package com.codefusion.wasbackend.notification.service;

import com.codefusion.wasbackend.exception.ResourceNotFoundException;
import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.mapper.NotificationMapper;
import com.codefusion.wasbackend.notification.model.NotificationEntity;
import com.codefusion.wasbackend.notification.repository.NotificationRepository;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findByIsDeleted(false)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationDTO getNotificationById(Long id) {
        NotificationEntity notification = notificationRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        return notificationMapper.toDto(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserEntity not found for id: " + userId));
        return notificationRepository.findByUserAndIsDeleted(userEntity, false)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByStore(Long storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("StoreEntity not found for id: " + storeId));
        return notificationRepository.findByStoreAndIsDeleted(storeEntity, false)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public NotificationDTO createNotification(NotificationDTO notificationDto) {
        NotificationEntity notification = notificationMapper.toEntity(notificationDto);
        NotificationEntity savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDto(savedNotification);
    }

    @Transactional
    public NotificationDTO updateNotification(Long id, NotificationDTO notificationDto) {
        NotificationEntity existingNotification = notificationRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        NotificationEntity notification = notificationMapper.toEntity(notificationDto);
        notification.setId(id);
        notification.setIsDeleted(existingNotification.getIsDeleted());
        NotificationEntity savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDto(savedNotification);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        NotificationEntity notification = notificationRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        notification.setIsDeleted(true);
        notificationRepository.save(notification);
    }
}