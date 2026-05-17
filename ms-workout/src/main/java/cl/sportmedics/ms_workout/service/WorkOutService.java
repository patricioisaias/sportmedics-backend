package cl.sportmedics.ms_workout.service;

import cl.sportmedics.ms_workout.model.Workout;
import cl.sportmedics.ms_workout.dto.WorkoutDTO;
import java.util.List;

public interface IWorkoutService {
    List<Workout> findAll();
    Workout findById(Long id);
    Workout save(WorkoutDTO workoutDTO);
    Workout update(Long id, WorkoutDTO workoutDTO);
    void delete(Long id);
}