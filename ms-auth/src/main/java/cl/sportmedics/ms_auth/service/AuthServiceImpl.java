package cl.sportmedics.ms_auth.service;

import cl.sportmedics.ms_auth.dto.AuthLoginDTO;
import cl.sportmedics.ms_auth.dto.AuthRegisterDTO;
import cl.sportmedics.ms_auth.dto.AuthResponseDTO;
import cl.sportmedics.ms_auth.entity.Credential;
import cl.sportmedics.ms_auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository repository;

    @Override
    public AuthResponseDTO register(AuthRegisterDTO dto) {
        log.info("Iniciando registro de credenciales para: {}", dto.getUsername());

        if (repository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }

        // 💡 EXPLICACIÓN PARA EL RAMO:
        // Aquí se usaría: passwordEncoder.encode(dto.getPassword())
        // Simulamos la encriptación para mantener la estructura limpia por ahora.
        String simulatedHash = "[BCRYPT_HASH_SIMULADO]_" + dto.getPassword();

        Credential credential = Credential.builder()
                .username(dto.getUsername())
                .passwordHash(simulatedHash)
                .role(dto.getRole().toUpperCase())
                .active(true)
                .build();

        repository.save(credential);
        log.info("Usuario registrado exitosamente en el sistema de Auth.");

        return AuthResponseDTO.builder()
                .username(credential.getUsername())
                .role(credential.getRole())
                .message("Registro exitoso.")
                .build();
    }

    @Override
    public AuthResponseDTO login(AuthLoginDTO dto) {
        log.info("Intento de inicio de sesión para: {}", dto.getUsername());

        Credential credential = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas."));

        // 💡 EXPLICACIÓN PARA EL RAMO:
        // Aquí se usaría passwordEncoder.matches(dto.getPassword(),
        // credential.getPasswordHash())
        String simulatedHash = "[BCRYPT_HASH_SIMULADO]_" + dto.getPassword();

        if (!credential.getPasswordHash().equals(simulatedHash)) {
            log.error("Contraseña incorrecta para el usuario: {}", dto.getUsername());
            throw new RuntimeException("Credenciales inválidas.");
        }

        if (!credential.getActive()) {
            throw new RuntimeException("La cuenta de usuario se encuentra inactiva.");
        }

        // Simulación de generación de Token JWT
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MockTokenFor" + credential.getUsername();
        log.info("Login exitoso, token generado.");

        return AuthResponseDTO.builder()
                .username(credential.getUsername())
                .role(credential.getRole())
                .token(mockToken)
                .message("Autenticación exitosa.")
                .build();
    }

    @Override
    public List<AuthResponseDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(cred -> AuthResponseDTO.builder()
                        .username(cred.getUsername())
                        .role(cred.getRole())
                        .message("Activo: " + cred.getActive())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        repository.deleteById(id);
        log.info("Credenciales eliminadas (ID: {})", id);
    }
}