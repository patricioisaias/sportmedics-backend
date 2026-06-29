package cl.sportmedics.ms_scheduling.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {
    private Long id;
    private Long memberId;
    private Long employeeId;
    private String activityName;
    private LocalDateTime scheduledDate;
    private String status;
}