package cl.sportmedics.ms_member.service;

import cl.sportmedics.ms_member.dto.MemberRequestDTO;
import cl.sportmedics.ms_member.dto.MemberResponseDTO;
import cl.sportmedics.ms_member.entity.Member;
import cl.sportmedics.ms_member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;

    @Override
    public MemberResponseDTO create(MemberRequestDTO dto) {
        log.info("Intentando registrar nuevo miembro con RUT: {}", dto.getRut());

        if (repository.findByRut(dto.getRut()).isPresent()) {
            throw new RuntimeException("El RUT ingresado ya está registrado.");
        }
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya se encuentra en uso.");
        }

        Member member = Member.builder()
                .rut(dto.getRut())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .active(dto.getActive())
                .build();

        Member saved = repository.save(member);
        log.info("Miembro registrado exitosamente con ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    public List<MemberResponseDTO> getAll() {
        log.info("Consultando la lista completa de miembros.");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public MemberResponseDTO getById(Long id) {
        log.info("Buscando miembro con ID: {}", id);
        return repository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Miembro no encontrado en los registros."));
    }

    @Override
    public MemberResponseDTO update(Long id, MemberRequestDTO dto) {
        log.info("Actualizando datos del miembro ID: {}", id);
        Member member = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Miembro no encontrado."));

        member.setRut(dto.getRut());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setActive(dto.getActive());

        log.info("Datos del miembro ID: {} actualizados.", id);
        return mapToDTO(repository.save(member));
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando miembro ID: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Miembro no encontrado.");
        }
        repository.deleteById(id);
        log.info("Miembro ID: {} eliminado del sistema.", id);
    }

    private MemberResponseDTO mapToDTO(Member member) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setId(member.getId());
        dto.setRut(member.getRut());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setActive(member.getActive());
        return dto;
    }
}