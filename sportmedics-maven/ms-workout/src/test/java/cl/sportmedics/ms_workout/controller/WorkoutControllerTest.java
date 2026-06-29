package cl.sportmedics.ms_workout.controller;

import cl.sportmedics.ms_workout.dto.WorkoutDetailDTO;
import cl.sportmedics.ms_workout.dto.WorkoutRequestDTO;
import cl.sportmedics.ms_workout.dto.WorkoutResponseDTO;
import cl.sportmedics.ms_workout.service.WorkoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(WorkoutController.class)
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutService workoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        // ARRANGE: preparar datos y mocks.
        WorkoutDetailDTO detail = new WorkoutDetailDTO();
        detail.setExerciseName("Press Banca");
        detail.setMuscleGroup("Pecho");
        detail.setSets(4);
        detail.setReps(10);
        detail.setRestSeconds(60);
        detail.setDayOfWeek("LUNES");

        WorkoutRequestDTO request = new WorkoutRequestDTO();
        request.setMemberId(1L);
        request.setTeacherId(2L);
        request.setName("Rutina Pecho");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusMonths(1));
        request.setActive(true);
        request.setDetails(List.of(detail));

        WorkoutResponseDTO response = new WorkoutResponseDTO();
        response.setId(1L);
        response.setName("Rutina Pecho");

        Mockito.when(workoutService.create(any(WorkoutRequestDTO.class))).thenReturn(response);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rutina Pecho"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(workoutService, Mockito.times(1)).create(any(WorkoutRequestDTO.class));
        
        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 201 Created
        // Se obtuvo: HTTP 400 Bad Request
        // Explicación: QA debería reportar que al enviar datos válidos, la API rechaza la solicitud. Desarrollo debería revisar las validaciones @Valid en el DTO o posibles campos faltantes.
    }

    @Test
    void testGetAll() throws Exception {
        // ARRANGE: preparar datos y mocks.
        WorkoutResponseDTO response1 = new WorkoutResponseDTO();
        response1.setId(1L);
        response1.setName("Rutina 1");

        WorkoutResponseDTO response2 = new WorkoutResponseDTO();
        response2.setId(2L);
        response2.setName("Rutina 2");

        Mockito.when(workoutService.getAll()).thenReturn(List.of(response1, response2));

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(get("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("Rutina 2"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(workoutService, Mockito.times(1)).getAll();

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 200 OK con una lista de rutinas
        // Se obtuvo: HTTP 500 Internal Server Error
        // Explicación: QA debería reportar que el endpoint falla al intentar obtener los datos. Desarrollo debería revisar si hay algún problema al mapear las entidades en el service.
    }

    @Test
    void testGetById() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long workoutId = 1L;
        WorkoutResponseDTO response = new WorkoutResponseDTO();
        response.setId(workoutId);
        response.setName("Rutina Piernas");

        Mockito.when(workoutService.getById(workoutId)).thenReturn(response);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(get("/api/workouts/{id}", workoutId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(workoutId))
                .andExpect(jsonPath("$.name").value("Rutina Piernas"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(workoutService, Mockito.times(1)).getById(workoutId);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 200 OK
        // Se obtuvo: HTTP 404 Not Found
        // Explicación: QA debería reportar que al buscar un ID existente, el sistema devuelve un error 404. Desarrollo debería revisar la lógica del Service y la consulta a la BD.
    }

    @Test
    void testDelete() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long workoutId = 1L;
        Mockito.doNothing().when(workoutService).delete(workoutId);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(delete("/api/workouts/{id}", workoutId))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(workoutService, Mockito.times(1)).delete(workoutId);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 204 No Content
        // Se obtuvo: HTTP 500 Internal Server Error
        // Explicación: QA reporta que al eliminar una rutina falla el servidor. Desarrollo debería revisar si hay una restricción de llave foránea en la BD que impide borrar la rutina.
    }
}
