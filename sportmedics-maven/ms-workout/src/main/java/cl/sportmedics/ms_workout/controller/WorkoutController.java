package cl.sportmedics.ms_workout.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


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
@Tag(name = "Workout", description = "Operaciones relacionadas con Workout")
@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService service;

    @Operation(summary = "Crear un nuevo registro", description = "Crear un nuevo registro en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> create(@Valid @RequestBody WorkoutRequestDTO dto) {
        log.info("Petición POST recibida en /api/workouts");
        WorkoutResponseDTO response = service.create(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<WorkoutResponseDTO>> getAll() {
        log.info("Petición GET recibida en /api/workouts");
        List<WorkoutResponseDTO> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> getById(@PathVariable Long id) {
        log.info("Petición GET recibida en /api/workouts/{}", id);
        WorkoutResponseDTO response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un registro", description = "Eliminar un registro en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Petición DELETE recibida en /api/workouts/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}