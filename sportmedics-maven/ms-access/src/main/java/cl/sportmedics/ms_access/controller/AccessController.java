package cl.sportmedics.ms_access.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.sportmedics.ms_access.dto.AccessRequestDTO;
import cl.sportmedics.ms_access.dto.AccessResponseDTO;
import cl.sportmedics.ms_access.service.AccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Tag(name = "Accesos", description = "Operaciones relacionadas con Accesos")
@RestController
@RequestMapping("/api/accesses")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService service;

    @Operation(summary = "Verificar e ingresar", description = "Verifica y registra un intento de acceso")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping("/verify")
    public ResponseEntity<AccessResponseDTO> verifyAndRegister(@Valid @RequestBody AccessRequestDTO dto) {
        log.info("Petición POST recibida en /api/accesses/verify");
        return new ResponseEntity<>(service.registerAccessAttempt(dto), HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los registros de acceso", description = "Retorna una lista de todos los registros de acceso registrados")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<AccessResponseDTO>> getAllLogs() {
        return ResponseEntity.ok(service.getAllAccessLogs());
    }

    @Operation(summary = "Obtener registros de acceso por miembro", description = "Retorna una lista de registros de acceso asociados a un miembro")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<AccessResponseDTO>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(service.getLogsByMemberId(memberId));
    }
}