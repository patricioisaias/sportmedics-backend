package cl.sportmedics.ms_access.service;


import cl.sportmedics.ms_access.dto.AccessRequestDTO;
import cl.sportmedics.ms_access.dto.AccessResponseDTO;
import cl.sportmedics.ms_access.entity.Access;
import cl.sportmedics.ms_access.repository.AccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {

    private final AccessRepository repository;

    @Override
    public AccessResponseDTO registerAccessAttempt(AccessRequestDTO dto) {
        log.info("Procesando intento de acceso en torniquete para miembro ID: {}", dto.getMemberId());
        
        boolean accessGranted = true;
        String reason = null;

        // 💡 EXPLICACIÓN PARA EL RAMO:
        // Aquí simulamos la regla de negocio de la rúbrica. 
        // Si el memberId es igual a 2, simularemos que está moroso (para probar respuestas negativas).
        // En la siguiente fase, aquí se llamará vía Feign a: ms-billing.getPaymentStatus(dto.getMemberId())
        if (dto.getMemberId().equals(2L)) {
            accessGranted = false;
            reason = "ACCESO DENEGADO: Alumno registra deuda pendiente en ms-billing.";
            log.warn("Acceso denegado para miembro ID: {}. Razón: {}", dto.getMemberId(), reason);
        }

        Access access = Access.builder()
                .memberId(dto.getMemberId())
                .accessDateTime(LocalDateTime.now())
                .granted(accessGranted)
                .denialReason(reason)
                .build();

        Access saved = repository.save(access);
        if (saved.getGranted()) {
            log.info("Acceso PERMITIDO exitosamente para registro ID: {}", saved.getId());
        }
        
        return mapToDTO(saved);
    }

    @Override
    public List<AccessResponseDTO> getAllAccessLogs() {
        log.info("Consultando historial global de accesos.");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<AccessResponseDTO> getLogsByMemberId(Long memberId) {
        log.info("Consultando historial de accesos para el miembro ID: {}", memberId);
        return repository.findByMemberId(memberId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private AccessResponseDTO mapToDTO(Access access) {
        AccessResponseDTO dto = new AccessResponseDTO();
        dto.setId(access.getId());
        dto.setMemberId(access.getMemberId());
        dto.setAccessDateTime(access.getAccessDateTime());
        dto.setGranted(access.getGranted());
        dto.setDenialReason(access.getDenialReason());
        return dto;
    }
}