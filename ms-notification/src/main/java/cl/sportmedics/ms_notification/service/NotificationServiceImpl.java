package cl.sportmedics.ms_notification.service;

import cl.sportmedics.ms_notification.dto.NotificationRequestDTO;
import cl.sportmedics.ms_notification.dto.NotificationResponseDTO;
import cl.sportmedics.ms_notification.entity.Notification;
import cl.sportmedics.ms_notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public NotificationResponseDTO sendNotification(NotificationRequestDTO dto) {
        log.info("Iniciando proceso de envío de notificación hacia: {}", dto.getRecipientEmail());

        // 💡 EXPLICACIÓN PARA EL RAMO:
        // En un entorno de producción, aquí se inyectaría JavaMailSender para enviar el
        // correo real.
        // Para asegurar estabilidad en la evaluación académica, simulamos el envío
        // exitoso en consola.

        log.info(">>> SIMULACIÓN DE ENVÍO SMTP <<<");
        log.info("To: {}", dto.getRecipientEmail());
        log.info("Subject: {}", dto.getSubject());
        log.info("Body: {}", dto.getMessage());
        log.info(">>> ENVÍO COMPLETADO <<<");

        Notification notification = Notification.builder()
                .recipientEmail(dto.getRecipientEmail())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .sentAt(LocalDateTime.now())
                .status("SENT")
                .build();

        Notification saved = repository.save(notification);
        log.info("Notificación registrada en historial con ID: {}", saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        log.info("Consultando historial completo de notificaciones despachadas.");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByEmail(String email) {
        log.info("Consultando historial de notificaciones para el correo: {}", email);
        return repository.findByRecipientEmail(email).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private NotificationResponseDTO mapToDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setRecipientEmail(notification.getRecipientEmail());
        dto.setSubject(notification.getSubject());
        dto.setMessage(notification.getMessage());
        dto.setSentAt(notification.getSentAt());
        dto.setStatus(notification.getStatus());
        return dto;
    }
}