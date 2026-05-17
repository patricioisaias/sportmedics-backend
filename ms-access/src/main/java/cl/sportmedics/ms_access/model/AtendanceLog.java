package cl.sportmedics.ms_access.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "access_type", nullable = false)
    private String accessType; // ENTRY, EXIT

    @Column(nullable = false)
    private Boolean granted;

    @Column(name = "denial_reason")
    private String denialReason;
}