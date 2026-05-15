package cl.sportmedics.ms_member.service;

import cl.sportmedics.ms_member.dto.MemberRequestDTO;
import cl.sportmedics.ms_member.dto.MemberResponseDTO;
import java.util.List;

public interface MemberService {
    MemberResponseDTO create(MemberRequestDTO dto);

    List<MemberResponseDTO> getAll();

    MemberResponseDTO getById(Long id);

    MemberResponseDTO update(Long id, MemberRequestDTO dto);

    void delete(Long id);
}