package cl.sportmedics.ms_access.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "accesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId; // Referencia lógica al alumno de ms-member

    @Column(name = "access_date_time", nullable = false)
    private LocalDateTime accessDateTime;

    @Column(nullable = false)
    private Boolean granted; // true = permitido, false = denegado

    @Column(name = "denial_reason")
    private String denialReason; // Motivo si fue denegado (ej: Moroso)
}