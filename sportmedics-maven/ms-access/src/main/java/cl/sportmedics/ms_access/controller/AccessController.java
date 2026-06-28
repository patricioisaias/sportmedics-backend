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
@Tag(name = "Access", description = "Operaciones relacionadas con Access")
@RestController
@RequestMapping("/api/accesses")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService service;

    @Operation(summary = "Crear un nuevo registro", description = "Crear un nuevo registro en el sistema")

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

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<AccessResponseDTO>> getAllLogs() {
        return ResponseEntity.ok(service.getAllAccessLogs());
    }

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

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