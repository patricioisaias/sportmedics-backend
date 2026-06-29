package cl.sportmedics.ms_notification.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private String status; // Ej: SENT, FAILED, PENDING
}