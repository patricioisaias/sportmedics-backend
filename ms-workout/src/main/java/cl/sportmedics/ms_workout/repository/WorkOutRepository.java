package cl.sportmedics.ms_workout.repository;

import cl.sportmedics.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    // Derived query para verificar si una rutina ya existe por nombre
    boolean existsByNameIgnoreCase(String name);
}
