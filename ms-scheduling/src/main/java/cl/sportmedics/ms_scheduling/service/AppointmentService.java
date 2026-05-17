package cl.sportmedics.ms_scheduling.service;

import cl.sportmedics.ms_scheduling.dto.AppointmentRequestDTO;
import cl.sportmedics.ms_scheduling.dto.AppointmentResponseDTO;
import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO create(AppointmentRequestDTO dto);

    List<AppointmentResponseDTO> getAll();

    AppointmentResponseDTO getById(Long id);

    List<AppointmentResponseDTO> getByMemberId(Long memberId);

    AppointmentResponseDTO updateStatus(Long id, String status);

    void delete(Long id);
}