package cl.sportmedics.ms_workout.dto;


import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutResponseDTO {
    private Long id;
    private Long memberId;
    private Long teacherId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private List<WorkoutDetailDTO> details;
}