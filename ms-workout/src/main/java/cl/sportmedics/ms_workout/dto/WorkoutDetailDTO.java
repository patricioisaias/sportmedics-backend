package cl.sportmedics.ms_workout.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkoutDetailDTO {
    @NotBlank(message = "El nombre del ejercicio es obligatorio.")
    private String exerciseName;

    @NotBlank(message = "El grupo muscular es obligatorio.")
    private String muscleGroup;

    @NotNull(message = "Las series son obligatorias.")
    @Min(value = 1, message = "Debe tener al menos 1 serie.")
    private Integer sets;

    @NotNull(message = "Las repeticiones son obligatorias.")
    @Min(value = 1, message = "Debe tener al menos 1 repetición.")
    private Integer reps;

    private Integer restSeconds;

    @NotBlank(message = "El día de la semana es obligatorio.")
    private String dayOfWeek;
}