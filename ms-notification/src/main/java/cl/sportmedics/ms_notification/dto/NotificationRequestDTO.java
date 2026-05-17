package cl.sportmedics.ms_notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationRequestDTO {
    @NotBlank(message = "El correo del destinatario es obligatorio.")
    @Email(message = "Debe proporcionar un correo válido.")
    private String recipientEmail;

    @NotBlank(message = "El asunto de la notificación es obligatorio.")
    private String subject;

    @NotBlank(message = "El mensaje no puede estar vacío.")
    private String message;
}