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

    @Test
    public void testGetAllAppointments() {
        // ARRANGE: preparar lista de entidades simuladas
        Appointment apt1 = new Appointment();
        apt1.setId(1L);
        apt1.setStatus("SCHEDULED");
        
        Appointment apt2 = new Appointment();
        apt2.setId(2L);
        apt2.setStatus("COMPLETED");

        when(repository.findAll()).thenReturn(java.util.Arrays.asList(apt1, apt2));

        // ACT: ejecutar método
        java.util.List<AppointmentResponseDTO> response = service.getAll();

        // ASSERT: verificar el resultado
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals(2L, response.get(1).getId());

        // VERIFY: comprobar llamadas al mock
        verify(repository).findAll();
    }

    @Test
    public void testGetById() {
        // ARRANGE: crear entidad envuelta en Optional
        Appointment apt = new Appointment();
        apt.setId(5L);
        apt.setStatus("COMPLETED");

        when(repository.findById(5L)).thenReturn(java.util.Optional.of(apt));

        // ACT: buscar por ID
        AppointmentResponseDTO response = service.getById(5L);

        // ASSERT: validar resultados
        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("COMPLETED", response.getStatus());

        // VERIFY: confirmar llamada al repositorio
        verify(repository).findById(5L);
    }

    @Test
    public void testGetByMemberId() {
        // ARRANGE: simular respuesta del historial del socio
        Appointment apt = new Appointment();
        apt.setId(7L);
        apt.setMemberId(1L);

        when(repository.findByMemberId(1L)).thenReturn(java.util.Collections.singletonList(apt));

        // ACT: solicitar historial del socio
        java.util.List<AppointmentResponseDTO> response = service.getByMemberId(1L);

        // ASSERT: debe venir 1 cita
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(7L, response.get(0).getId());

        // VERIFY
        verify(repository).findByMemberId(1L);
    }

    @Test
    public void testUpdateStatus() {
        // ARRANGE: simular cita existente en estado SCHEDULED
        Appointment apt = new Appointment();
        apt.setId(5L);
        apt.setStatus("SCHEDULED");

        // Mock 1: Al buscarla, devolver la entidad original
        when(repository.findById(5L)).thenReturn(java.util.Optional.of(apt));
        
        // Mock 2: Al guardarla, devolverla (se asume que el servicio le cambió el estado)
        when(repository.save(any(Appointment.class))).thenReturn(apt);

        // ACT: solicitar cambio de estado a COMPLETED
        AppointmentResponseDTO response = service.updateStatus(5L, "COMPLETED");

        // ASSERT: validar que la respuesta refleja el nuevo estado
        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("COMPLETED", response.getStatus());

        // VERIFY: confirmar flujo de lectura -> actualización
        verify(repository).findById(5L);
        verify(repository).save(any(Appointment.class));
    }

    @Test
    public void testDelete() {
        // ARRANGE: simular que la cita existe en BD
        when(repository.existsById(1L)).thenReturn(true);

        // ACT: ejecutar eliminación
        service.delete(1L);

        // VERIFY: confirmar que se verificó existencia y luego se borró
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }
}
/*
 Caso hipotético de falla para QA:
 Se esperaba: Un objeto AppointmentResponseDTO con ID=10L
 Se obtuvo: NullPointerException
 
 Cómo reportarlo (QA): El test unitario arroja NPE al guardar.
 Qué revisar (Desarrollo): Revisar si el mapToDTO falla al recibir parámetros nulos desde la base de datos o si un objeto relacionado viene vacío.
*/
