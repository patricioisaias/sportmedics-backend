package cl.sportmedics.ms_workout.controller;

import cl.sportmedics.workout.dto.WorkoutDTO;
import cl.sportmedics.workout.model.Workout;
import cl.sportmedics.workout.service.IWorkoutService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@Slf4j
public class WorkoutController {

    @Autowired
    private IWorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<Workout>> getAll() {
        log.info("MS-WORKOUT [Controller]: Petición GET recibida para listar todas las rutinas");
        return ResponseEntity.ok(workoutService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> getById(@PathVariable Long id) {
        log.info("MS-WORKOUT [Controller]: Petición GET recibida para ID: {}", id);
        return ResponseEntity.ok(workoutService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Workout> create(@Valid @RequestBody WorkoutDTO workoutDTO) {
        log.info("MS-WORKOUT [Controller]: Petición POST recibida para crear rutina: {}", workoutDTO.getName());
        Workout nuevaRutina = workoutService.save(workoutDTO);
        return new ResponseEntity<>(nuevaRutina, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> update(@PathVariable Long id, @Valid @RequestBody WorkoutDTO workoutDTO) {
        log.info("MS-WORKOUT [Controller]: Petición PUT recibida para actualizar ID: {}", id);
        return ResponseEntity.ok(workoutService.update(id, workoutDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("MS-WORKOUT [Controller]: Petición DELETE recibida para ID: {}", id);
        workoutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}