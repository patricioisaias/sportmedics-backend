package cl.sportmedics.ms_notification.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {
    private Long id;
    private String recipientEmail;
    private String subject;
    private String message;
    private LocalDateTime sentAt;
    private String status;
}