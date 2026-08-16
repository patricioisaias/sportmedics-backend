package cl.sportmedics.ms_workout.service;

import cl.sportmedics.ms_workout.dto.WorkoutDetailDTO;
import cl.sportmedics.ms_workout.dto.WorkoutRequestDTO;
import cl.sportmedics.ms_workout.dto.WorkoutResponseDTO;
import cl.sportmedics.ms_workout.entity.Workout;
import cl.sportmedics.ms_workout.repository.WorkoutRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository repository;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @Test
    void testCreate() {
        // ARRANGE: preparar datos y mocks.
        WorkoutDetailDTO detailDTO = new WorkoutDetailDTO();
        detailDTO.setExerciseName("Sentadillas");
        detailDTO.setMuscleGroup("Piernas");
        detailDTO.setSets(4);
        detailDTO.setReps(12);
        detailDTO.setRestSeconds(60);
        detailDTO.setDayOfWeek("LUNES");

        WorkoutRequestDTO request = new WorkoutRequestDTO();
        request.setMemberId(1L);
        request.setTeacherId(2L);
        request.setName("Rutina Fuerza");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusMonths(1));
        request.setActive(true);
        request.setDetails(List.of(detailDTO));

        Workout savedWorkout = Workout.builder()
                .id(1L)
                .memberId(1L)
                .teacherId(2L)
                .name("Rutina Fuerza")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .active(true)
                .details(new ArrayList<>())
                .build();

        Mockito.when(repository.save(any(Workout.class))).thenReturn(savedWorkout);

        // ACT: ejecutar método o endpoint.
        WorkoutResponseDTO response = workoutService.create(request);

        // ASSERT: verificar resultado esperado.
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Rutina Fuerza", response.getName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).save(any(Workout.class));

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: Creación exitosa del WorkoutResponseDTO
        // Se obtuvo: NullPointerException
        // Explicación: QA probará el comportamiento en caso de fallo, desarrollo debería revisar que no hayan mapeos problemáticos o detalles en null si no se validan antes.
    }

    @Test
    void testGetAll() {
        // ARRANGE: preparar datos y mocks.
        Workout workout1 = Workout.builder().id(1L).name("Rutina 1").details(new ArrayList<>()).build();
        Workout workout2 = Workout.builder().id(2L).name("Rutina 2").details(new ArrayList<>()).build();

        Mockito.when(repository.findAll()).thenReturn(List.of(workout1, workout2));

        // ACT: ejecutar método o endpoint.
        List<WorkoutResponseDTO> responses = workoutService.getAll();

        // ASSERT: verificar resultado esperado.
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Rutina 1", responses.get(0).getName());
        assertEquals(1L, responses.get(0).getId());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).findAll();

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: Una lista de DTOs mapeados.
        // Se obtuvo: NullPointerException.
        // Explicación: QA reporta fallo al listar rutinas. Desarrollo debe revisar el mapeo a DTO; si la colección 'details' es null en la base de datos, '.stream()' lanzará NullPointerException.
    }

    @Test
    void testGetById() {
        // ARRANGE: preparar datos y mocks.
        Long workoutId = 1L;
        Workout workout = Workout.builder().id(workoutId).name("Rutina Biceps").details(new ArrayList<>()).build();

        Mockito.when(repository.findById(workoutId)).thenReturn(java.util.Optional.of(workout));

        // ACT: ejecutar método o endpoint.
        WorkoutResponseDTO response = workoutService.getById(workoutId);

        // ASSERT: verificar resultado esperado.
        assertNotNull(response);
        assertEquals(workoutId, response.getId());
        assertEquals("Rutina Biceps", response.getName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).findById(workoutId);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: La rutina mapeada a DTO.
        // Se obtuvo: RuntimeException("Entrenamiento no encontrado.")
        // Explicación: QA prueba buscar un ID que no existe en el sistema y recibe un error 500. Desarrollo debería revisar si se necesita cambiar RuntimeException por un error 404 manejado (ej. ResourceNotFoundException).
    }

    @Test
    void testDelete() {
        // ARRANGE: preparar datos y mocks.
        Long workoutId = 1L;
        Mockito.when(repository.existsById(workoutId)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(workoutId);

        // ACT: ejecutar método o endpoint.
        assertDoesNotThrow(() -> workoutService.delete(workoutId));

        // ASSERT: verificar resultado esperado.
        // El método delete es void, verificamos que no lance excepciones.

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).existsById(workoutId);
        Mockito.verify(repository, Mockito.times(1)).deleteById(workoutId);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: Eliminación exitosa sin lanzar excepciones.
        // Se obtuvo: RuntimeException("Entrenamiento no encontrado.")
        // Explicación: QA prueba eliminar un ID que no existe. Desarrollo debería evaluar si el error debe lanzar RuntimeException, lo que desencadena un 500, en lugar de un manejo adecuado de 404.
    }
}
