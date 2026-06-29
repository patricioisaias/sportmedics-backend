package cl.sportmedics.ms_scheduling.controller;

import cl.sportmedics.ms_scheduling.dto.AppointmentRequestDTO;
import cl.sportmedics.ms_scheduling.dto.AppointmentResponseDTO;
import cl.sportmedics.ms_scheduling.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService service;

    @Test
    public void testCreateAppointment() throws Exception {
        // ARRANGE: preparar datos y mocks
        AppointmentResponseDTO responseDto = new AppointmentResponseDTO();
        responseDto.setId(10L);
        responseDto.setStatus("SCHEDULED");
        
        when(service.create(any(AppointmentRequestDTO.class))).thenReturn(responseDto);

        String validJson = "{\"memberId\": 1, \"employeeId\": 2, \"activityName\": \"Entrenamiento Funcional\", \"scheduledDate\": \"2030-12-01T15:30:00\", \"status\": \"SCHEDULED\"}";

        // ACT: ejecutar método o endpoint
        mockMvc.perform(post("/api/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

        // VERIFY: comprobar llamadas al mock
        verify(service).create(any(AppointmentRequestDTO.class));
    }

    @Test
    public void testGetAllAppointments() throws Exception {
        // ARRANGE: preparar lista simulada
        AppointmentResponseDTO dto1 = new AppointmentResponseDTO();
        dto1.setId(1L);
        dto1.setStatus("SCHEDULED");
        
        AppointmentResponseDTO dto2 = new AppointmentResponseDTO();
        dto2.setId(2L);
        dto2.setStatus("COMPLETED");

        when(service.getAll()).thenReturn(java.util.Arrays.asList(dto1, dto2));

        // ACT & ASSERT: ejecutar GET y verificar la lista
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/schedules")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        // VERIFY: comprobar llamadas al mock
        verify(service).getAll();
    }

    @Test
    public void testGetById() throws Exception {
        // ARRANGE: preparar DTO simulado
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(5L);
        dto.setStatus("COMPLETED");

        when(service.getById(5L)).thenReturn(dto);

        // ACT & ASSERT: ejecutar petición y verificar mapeo JSON
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/schedules/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        // VERIFY
        verify(service).getById(5L);
    }

    @Test
    public void testGetByMemberId() throws Exception {
        // ARRANGE: simular lista de citas del socio 1
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(7L);
        dto.setStatus("SCHEDULED");

        when(service.getByMemberId(1L)).thenReturn(java.util.Collections.singletonList(dto));

        // ACT & ASSERT: ejecutar y validar que trae 1 elemento
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/schedules/member/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(7));

        // VERIFY
        verify(service).getByMemberId(1L);
    }

    @Test
    public void testUpdateStatus() throws Exception {
        // ARRANGE: preparar mock
        AppointmentResponseDTO responseDto = new AppointmentResponseDTO();
        responseDto.setId(5L);
        responseDto.setStatus("COMPLETED");

        when(service.updateStatus(5L, "COMPLETED")).thenReturn(responseDto);

        // ACT & ASSERT: ejecutar PATCH enviando el param en la URL
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/schedules/5/status")
                .param("status", "COMPLETED")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        // VERIFY
        verify(service).updateStatus(5L, "COMPLETED");
    }

    @Test
    public void testDelete() throws Exception {
        // ARRANGE: nada que preparar porque el servicio devuelve void
        // y el controlador también. No necesitamos usar 'when()'.

        // ACT & ASSERT: ejecutar DELETE y verificar 204 No Content
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/schedules/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // VERIFY: confirmar que llegó la orden de borrar al mock del servicio
        verify(service).delete(1L);
    }
}
/*
 Caso hipotético de falla para QA:
 Se esperaba: HTTP 201 Created y JSON con status="SCHEDULED"
 Se obtuvo: HTTP 400 Bad Request
 
 Cómo reportarlo (QA): El endpoint POST /api/schedules falla con 400 Bad Request enviando un payload completo y válido.
 Qué revisar (Desarrollo): Revisar si la estructura o los formatos de fecha esperados por AppointmentRequestDTO coinciden con el JSON que se envió (ej. formato ISO 8601).
*/
