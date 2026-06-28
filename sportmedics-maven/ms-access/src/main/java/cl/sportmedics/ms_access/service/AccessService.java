package cl.sportmedics.ms_access.service;


import cl.sportmedics.ms_access.dto.AccessRequestDTO;
import cl.sportmedics.ms_access.dto.AccessResponseDTO;
import java.util.List;

public interface AccessService {
    AccessResponseDTO registerAccessAttempt(AccessRequestDTO dto);
    List<AccessResponseDTO> getAllAccessLogs();
    List<AccessResponseDTO> getLogsByMemberId(Long memberId);
}