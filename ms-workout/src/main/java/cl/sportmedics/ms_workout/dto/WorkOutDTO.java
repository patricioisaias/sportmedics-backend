package cl.sportmedics.ms_workout.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDTO {
    @NotBlank(message = "El nombre de la rutina es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @NotBlank(message = "El objetivo de la rutina es obligatorio")
    private String objective;

    @NotNull(message = "La cantidad de series no puede ser nula")
    @Min(value = 1, message = "Debe asignar al menos 1 serie")
    private Integer series;

    @NotNull(message = "La cantidad de repeticiones no puede ser nula")
    @Min(value = 1, message = "Debe asignar al menos 1 repetición")
    private Integer repetitions;

    @NotBlank(message = "El nivel es obligatorio (Principiante, Intermedio, Avanzado)")
    private String level;

}
