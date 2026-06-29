package cl.sportmedics.ms_notification.controller;

import cl.sportmedics.ms_notification.dto.NotificationRequestDTO;
import cl.sportmedics.ms_notification.dto.NotificationResponseDTO;
import cl.sportmedics.ms_notification.service.NotificationService;
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

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService service;

    @Test
    public void testSendNotification() throws Exception {
        // ARRANGE: preparar datos y mocks
        NotificationResponseDTO responseDto = new NotificationResponseDTO();
        responseDto.setId(10L);
        responseDto.setStatus("SENT");
        
        when(service.sendNotification(any(NotificationRequestDTO.class))).thenReturn(responseDto);

        String validJson = "{\"recipientEmail\": \"test@test.com\", \"subject\": \"Bienvenido\", \"message\": \"Hola\"}";

        // ACT: ejecutar método o endpoint
        mockMvc.perform(post("/api/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("SENT"));

        // VERIFY: comprobar llamadas al mock
        verify(service).sendNotification(any(NotificationRequestDTO.class));
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        // ARRANGE: preparamos un mock de lista de notificaciones
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(1L);
        dto.setStatus("SENT");
        
        when(service.getAllNotifications()).thenReturn(java.util.Collections.singletonList(dto));

        // ACT & ASSERT: llamamos a GET /api/notifications
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        // VERIFY: aseguramos el llamado al servicio
        verify(service).getAllNotifications();
    }

    @Test
    public void testGetByEmail() throws Exception {
        // ARRANGE: preparamos mock buscando por email
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(2L);
        dto.setStatus("FAILED");
        
        when(service.getNotificationsByEmail("test@test.com")).thenReturn(java.util.Collections.singletonList(dto));

        // ACT & ASSERT: llamamos enviando el param 'email'
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/notifications/email")
                .param("email", "test@test.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));

        // VERIFY: verificamos llamada al servicio con el parametro correcto
        verify(service).getNotificationsByEmail("test@test.com");
    }
}
/*
 Caso hipotético de falla para QA:
 Se esperaba: HTTP 201 Created y JSON con status="SENT"
 Se obtuvo: HTTP 400 Bad Request
 
 Cómo reportarlo (QA): El endpoint POST /api/notifications/send falla con 400 Bad Request enviando un payload completo y válido.
 Qué revisar (Desarrollo): Revisar si la estructura esperada por NotificationRequestDTO coincide con el JSON que se envió.
*/
