package cl.sportmedics.ms_auth.controller;

import cl.sportmedics.ms_auth.dto.AuthLoginDTO;
import cl.sportmedics.ms_auth.dto.AuthRegisterDTO;
import cl.sportmedics.ms_auth.dto.AuthResponseDTO;
import cl.sportmedics.ms_auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRegisterDTO dto) {
        log.info("Petición POST recibida en /api/auth/register");
        return new ResponseEntity<>(service.register(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginDTO dto) {
        log.info("Petición POST recibida en /api/auth/login");
        return ResponseEntity.ok(service.login(dto));
    }

    @GetMapping("/users")
    public ResponseEntity<List<AuthResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}