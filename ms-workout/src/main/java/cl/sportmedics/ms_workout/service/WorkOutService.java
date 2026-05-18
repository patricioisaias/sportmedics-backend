package cl.sportmedics.ms_workout.service;


import cl.sportmedics.ms_workout.dto.WorkoutRequestDTO;
import cl.sportmedics.ms_workout.dto.WorkoutResponseDTO;
import java.util.List;

public interface WorkoutService {
    WorkoutResponseDTO create(WorkoutRequestDTO dto);
    List<WorkoutResponseDTO> getAll();
    WorkoutResponseDTO getById(Long id);
    void delete(Long id);
}
