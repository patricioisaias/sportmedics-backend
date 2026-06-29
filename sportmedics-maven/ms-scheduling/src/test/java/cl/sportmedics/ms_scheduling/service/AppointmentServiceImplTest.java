package cl.sportmedics.ms_scheduling.service;

import cl.sportmedics.ms_scheduling.dto.AppointmentRequestDTO;
import cl.sportmedics.ms_scheduling.dto.AppointmentResponseDTO;
import cl.sportmedics.ms_scheduling.entity.Appointment;
import cl.sportmedics.ms_scheduling.repository.AppointmentRepository;
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
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentServiceImpl service;

    @Test
    public void testCreateAppointment() {
        // ARRANGE: preparar datos y mocks
        AppointmentRequestDTO request = new AppointmentRequestDTO();
        request.setMemberId(1L);
        request.setEmployeeId(2L);
        request.setActivityName("Yoga");
        request.setScheduledDate(LocalDateTime.now().plusDays(5));
        request.setStatus("SCHEDULED");

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(10L);
        savedAppointment.setMemberId(1L);
        savedAppointment.setEmployeeId(2L);
        savedAppointment.setActivityName("Yoga");
        savedAppointment.setScheduledDate(request.getScheduledDate());
        savedAppointment.setStatus("SCHEDULED");

        when(repository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // ACT: ejecutar método o endpoint
        AppointmentResponseDTO response = service.create(request);

        // ASSERT: verificar resultado esperado
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("SCHEDULED", response.getStatus());
        assertEquals("Yoga", response.getActivityName());

        // VERIFY: comprobar llamadas al mock
        verify(repository).save(any(Appointment.class));
    }
}
/*
 Caso hipotético de falla para QA:
 Se esperaba: Un objeto AppointmentResponseDTO con ID=10L
 Se obtuvo: NullPointerException
 
 Cómo reportarlo (QA): El test unitario arroja NPE al guardar.
 Qué revisar (Desarrollo): Revisar si el mapToDTO falla al recibir parámetros nulos desde la base de datos o si un objeto relacionado viene vacío.
*/
