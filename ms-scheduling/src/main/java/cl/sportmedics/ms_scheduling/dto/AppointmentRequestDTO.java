package cl.sportmedics.ms_scheduling.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {
    @NotNull(message = "El ID del alumno es obligatorio.")
    private Long memberId;

    @NotNull(message = "El ID del profesor/empleado es obligatorio.")
    private Long employeeId;

    @NotBlank(message = "El nombre de la actividad es obligatorio.")
    private String activityName;

    @NotNull(message = "La fecha y hora de la cita son obligatorias.")
    @FutureOrPresent(message = "No puedes agendar una cita en el pasado.")
    private LocalDateTime scheduledDate;

    private String status = "SCHEDULED";
}