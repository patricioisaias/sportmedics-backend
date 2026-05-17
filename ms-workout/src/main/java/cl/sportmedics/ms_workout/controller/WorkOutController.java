package cl.sportmedics.ms_workout.controller;


import cl.sportmedics.ms_workout.dto.WorkoutRequestDTO;
import cl.sportmedics.ms_workout.dto.WorkoutResponseDTO;
import cl.sportmedics.ms_workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService service;

    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> create(@Valid @RequestBody WorkoutRequestDTO dto) {
        log.info("Petición POST recibida en /api/workouts");
        WorkoutResponseDTO response = service.create(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponseDTO>> getAll() {
        log.info("Petición GET recibida en /api/workouts");
        List<WorkoutResponseDTO> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> getById(@PathVariable Long id) {
        log.info("Petición GET recibida en /api/workouts/{}", id);
        WorkoutResponseDTO response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Petición DELETE recibida en /api/workouts/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}