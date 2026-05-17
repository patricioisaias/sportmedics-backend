package cl.sportmedics.ms_workout.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutRequestDTO {
    @NotNull(message = "El ID del miembro es obligatorio.")
    private Long memberId;

    @NotNull(message = "El ID del profesor es obligatorio.")
    private Long teacherId;

    @NotBlank(message = "El nombre del entrenamiento es obligatorio.")
    private String name;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria.")
    private LocalDate endDate;

    private Boolean active = true;

    @Valid
    @NotNull(message = "El entrenamiento debe contener ejercicios.")
    private List<WorkoutDetailDTO> details;
}