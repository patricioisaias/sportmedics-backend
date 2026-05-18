package cl.sportmedics.ms_access.service;

import cl.sportmedics.ms_access.client.BillingFeignClient;
import cl.sportmedics.ms_access.client.MemberFeignClient;
import cl.sportmedics.ms_access.dto.AccessRequestDTO;
import cl.sportmedics.ms_access.dto.AccessResponseDTO;
import cl.sportmedics.ms_access.dto.BillingStatusDTO;
import cl.sportmedics.ms_access.dto.MemberDTO;
import cl.sportmedics.ms_access.entity.Access;
import cl.sportmedics.ms_access.repository.AccessRepository;
import feign.FeignException;
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
    private final MemberFeignClient memberClient;
    private final BillingFeignClient billingClient;

    @Override
    public AccessResponseDTO registerAccessAttempt(AccessRequestDTO dto) {
        log.info("Procesando intento de acceso en torniquete para miembro ID: {}", dto.getMemberId());

        boolean accessGranted = false;
        String reason = null;

        try {
            // 1. Validar ms-member: Existe y está Activo
            MemberDTO member = memberClient.getMemberById(dto.getMemberId());

            if (!"Activo".equalsIgnoreCase(member.getStatus())) {
                reason = "ACCESO DENEGADO: El socio no se encuentra en estado Activo.";
                log.warn("Acceso denegado para miembro ID: {}. Razón: {}", dto.getMemberId(), reason);
            } else {
                // 2. Validar ms-billing: Última boleta pagada
                BillingStatusDTO billing = billingClient.getBillingStatus(dto.getMemberId());

                if (billing.isHasDebt()) {
                    reason = "ACCESO DENEGADO: El socio registra mensualidades pendientes.";
                    log.warn("Acceso denegado para miembro ID: {}. Razón: {}", dto.getMemberId(), reason);
                } else {
                    // Pasa todas las validaciones
                    accessGranted = true;
                }
            }

        } catch (FeignException.NotFound e) {
            // Se captura si ms-member o ms-billing devuelven un 404
            reason = "ACCESO DENEGADO: Socio no encontrado en los registros.";
            log.error("Socio ID {} no encontrado: {}", dto.getMemberId(), e.getMessage());
        } catch (Exception e) {
            // Captura errores de conexión (ej. microservicio caído)
            reason = "ACCESO DENEGADO: Error temporal al validar datos del socio.";
            log.error("Error al comunicar con otros MS para socio ID {}: {}", dto.getMemberId(), e.getMessage());
        }

        // 3. Registrar el intento de acceso en la BD
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
        AccessResponseDTO responseDTO = new AccessResponseDTO();
        responseDTO.setId(access.getId());
        responseDTO.setMemberId(access.getMemberId());
        responseDTO.setAccessDateTime(access.getAccessDateTime());
        responseDTO.setGranted(access.getGranted());
        responseDTO.setDenialReason(access.getDenialReason());
        return responseDTO;
    }
}