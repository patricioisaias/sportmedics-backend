package cl.sportmedics.ms_auth.service;

import cl.sportmedics.ms_auth.dto.AuthLoginDTO;
import cl.sportmedics.ms_auth.dto.AuthRegisterDTO;
import cl.sportmedics.ms_auth.dto.AuthResponseDTO;
import java.util.List;

public interface AuthService {
    AuthResponseDTO register(AuthRegisterDTO dto);

    AuthResponseDTO login(AuthLoginDTO dto);

    List<AuthResponseDTO> getAllUsers();

    void deleteUser(Long id);
}