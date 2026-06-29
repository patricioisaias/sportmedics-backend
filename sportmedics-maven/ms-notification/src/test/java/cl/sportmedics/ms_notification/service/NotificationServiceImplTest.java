package cl.sportmedics.ms_notification.service;

import cl.sportmedics.ms_notification.dto.NotificationRequestDTO;
import cl.sportmedics.ms_notification.dto.NotificationResponseDTO;
import cl.sportmedics.ms_notification.entity.Notification;
import cl.sportmedics.ms_notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationServiceImpl service;

    @Test
    public void testSendNotification() {
        // ARRANGE: preparar datos y mocks
        NotificationRequestDTO request = new NotificationRequestDTO();
        request.setRecipientEmail("test@test.com");
        request.setSubject("Bienvenido");
        request.setMessage("Hola");

        Notification savedNotification = new Notification();
        savedNotification.setId(10L);
        savedNotification.setRecipientEmail("test@test.com");
        savedNotification.setSubject("Bienvenido");
        savedNotification.setMessage("Hola");
        savedNotification.setSentAt(LocalDateTime.now());
        savedNotification.setStatus("SENT");

        when(repository.save(any(Notification.class))).thenReturn(savedNotification);

        // ACT: ejecutar método o endpoint
        NotificationResponseDTO response = service.sendNotification(request);

        // ASSERT: verificar resultado esperado
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("SENT", response.getStatus());
        assertEquals("test@test.com", response.getRecipientEmail());

        // VERIFY: comprobar llamadas al mock
        verify(repository).save(any(Notification.class));
    }

    @Test
    public void testGetAllNotifications() {
        // ARRANGE: simular respuesta del historial
        Notification n = new Notification();
        n.setId(1L);
        n.setRecipientEmail("test1@test.com");
        
        when(repository.findAll()).thenReturn(java.util.Collections.singletonList(n));

        // ACT: solicitar todas
        java.util.List<NotificationResponseDTO> response = service.getAllNotifications();

        // ASSERT: validar mapeo
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals("test1@test.com", response.get(0).getRecipientEmail());

        // VERIFY: certificar llamada a BD
        verify(repository).findAll();
    }

    @Test
    public void testGetNotificationsByEmail() {
        // ARRANGE: simular respuesta por correo
        Notification n = new Notification();
        n.setId(2L);
        n.setRecipientEmail("test2@test.com");
        
        when(repository.findByRecipientEmail("test2@test.com")).thenReturn(java.util.Collections.singletonList(n));

        // ACT: solicitar filtrado
        java.util.List<NotificationResponseDTO> response = service.getNotificationsByEmail("test2@test.com");

        // ASSERT: validar mapeo
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(2L, response.get(0).getId());
        assertEquals("test2@test.com", response.get(0).getRecipientEmail());

        // VERIFY: certificar llamada filtrada a BD
        verify(repository).findByRecipientEmail("test2@test.com");
    }
}
/*
 Caso hipotético de falla para QA:
 Se esperaba: Un objeto NotificationResponseDTO con ID=10L
 Se obtuvo: NullPointerException
 
 Cómo reportarlo (QA): El test unitario arroja NPE al guardar.
 Qué revisar (Desarrollo): Revisar si el mapToDTO falla al recibir parámetros nulos desde la base de datos o si un objeto relacionado viene vacío.
*/
