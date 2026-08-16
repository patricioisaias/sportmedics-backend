package cl.sportmedics.ms_scheduling.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId; // Referencia a ms-member

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // Referencia a ms-employee

    @Column(name = "activity_name", nullable = false)
    private String activityName; // Ej: Evaluación, Spinning, Crossfit

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(nullable = false)
    private String status; // Ej: SCHEDULED, CANCELLED, COMPLETED
}