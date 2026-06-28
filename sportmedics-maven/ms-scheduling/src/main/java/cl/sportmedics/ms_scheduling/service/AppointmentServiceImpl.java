package cl.sportmedics.ms_scheduling.service;

import cl.sportmedics.ms_scheduling.dto.AppointmentRequestDTO;
import cl.sportmedics.ms_scheduling.dto.AppointmentResponseDTO;
import cl.sportmedics.ms_scheduling.entity.Appointment;
import cl.sportmedics.ms_scheduling.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;

    @Override
    public AppointmentResponseDTO create(AppointmentRequestDTO dto) {
        log.info("Agendando nueva cita para el alumno ID: {} con el profesor ID: {}", dto.getMemberId(),
                dto.getEmployeeId());

        // Más adelante, aquí agregaremos Feign Client para verificar si memberId y
        // employeeId realmente existen.

        Appointment appointment = Appointment.builder()
                .memberId(dto.getMemberId())
                .employeeId(dto.getEmployeeId())
                .activityName(dto.getActivityName())
                .scheduledDate(dto.getScheduledDate())
                .status(dto.getStatus().toUpperCase())
                .build();

        Appointment saved = repository.save(appointment);
        log.info("Cita agendada exitosamente con ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    public List<AppointmentResponseDTO> getAll() {
        log.info("Consultando todas las citas agendadas.");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO getById(Long id) {
        log.info("Buscando cita con ID: {}", id);
        return repository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));
    }

    @Override
    public List<AppointmentResponseDTO> getByMemberId(Long memberId) {
        log.info("Buscando historial de citas para el alumno ID: {}", memberId);
        return repository.findByMemberId(memberId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO updateStatus(Long id, String status) {
        log.info("Actualizando estado de la cita ID: {} a {}", id, status);
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        appointment.setStatus(status.toUpperCase());
        log.info("Estado de la cita actualizado correctamente.");
        return mapToDTO(repository.save(appointment));
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando cita ID: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada.");
        }
        repository.deleteById(id);
        log.info("Cita eliminada del sistema.");
    }

    private AppointmentResponseDTO mapToDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(appointment.getId());
        dto.setMemberId(appointment.getMemberId());
        dto.setEmployeeId(appointment.getEmployeeId());
        dto.setActivityName(appointment.getActivityName());
        dto.setScheduledDate(appointment.getScheduledDate());
        dto.setStatus(appointment.getStatus());
        return dto;
    }
}