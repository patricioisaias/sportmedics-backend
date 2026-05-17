package cl.sportmedics.ms_notification.service;

import cl.sportmedics.ms_notification.dto.NotificationRequestDTO;
import cl.sportmedics.ms_notification.dto.NotificationResponseDTO;
import java.util.List;

public interface NotificationService {
    NotificationResponseDTO sendNotification(NotificationRequestDTO dto);

    List<NotificationResponseDTO> getAllNotifications();

    List<NotificationResponseDTO> getNotificationsByEmail(String email);
}