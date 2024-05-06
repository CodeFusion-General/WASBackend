package com.codefusion.wasbackend.notification.dto;
import com.codefusion.wasbackend.notification.model.NotificationLevel;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import lombok.*;
import java.util.Date;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private Date recordDate;
    private String subject;
    private String description;
    private Long telegramId;
    private Boolean isSent;
    private Boolean isTelegram;
    private String text;
    private UserEntity user;
    private StoreEntity store;
    private Set<NotificationLevel> notificationLevel;


}