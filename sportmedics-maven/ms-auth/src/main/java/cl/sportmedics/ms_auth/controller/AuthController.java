package cl.sportmedics.ms_auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


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
@Tag(name = "Autenticación", description = "Operaciones relacionadas con Autenticación")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @Operation(summary = "Registrarse", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRegisterDTO dto) {
        log.info("Petición POST recibida en /api/auth/register");
        return new ResponseEntity<>(service.register(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión", description = "Inicia sesión en el sistema y retorna un token")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginDTO dto) {
        log.info("Petición POST recibida en /api/auth/login");
        return ResponseEntity.ok(service.login(dto));
    }

    @Operation(summary = "Obtener todos los sesiones", description = "Retorna una lista de todos los sesiones registrados")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/users")
    public ResponseEntity<List<AuthResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @Operation(summary = "Eliminar una sesión", description = "Elimina una sesión del sistema por ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}