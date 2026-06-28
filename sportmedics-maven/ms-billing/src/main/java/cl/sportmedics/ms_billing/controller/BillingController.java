package cl.sportmedics.ms_billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.sportmedics.ms_billing.dto.BillingRequestDTO;
import cl.sportmedics.ms_billing.dto.BillingResponseDTO;
import cl.sportmedics.ms_billing.dto.BillingStatusDTO;
import cl.sportmedics.ms_billing.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "Billing", description = "Operaciones relacionadas con Billing")
@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    @Operation(summary = "Crear un nuevo registro", description = "Crear un nuevo registro en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PostMapping
    public ResponseEntity<BillingResponseDTO> create(@Valid @RequestBody BillingRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<BillingResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Actualizar un registro existente", description = "Actualizar un registro existente en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PutMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BillingRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar un registro", description = "Eliminar un registro en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Crear un nuevo registro", description = "Crear un nuevo registro en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PostMapping("/{id}/payments")
    public ResponseEntity<Map<String, String>> pay(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Double amount = Double.valueOf(payload.get("amount").toString());
        String method = payload.get("method").toString();
        String ref = payload.get("reference") != null ? payload.get("reference").toString() : null;

        service.processPayment(id, amount, method, ref);
        return ResponseEntity.ok(Map.of("message", "Pago procesado correctamente."));
    }

    // ENDPOINT 1: Para que ms-access (El Torniquete) pregunte si hay deuda
    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/status/{memberId}")
    public ResponseEntity<BillingStatusDTO> getStatus(@PathVariable Long memberId) {
        boolean hasDebt = service.checkDebtStatus(memberId);
        BillingStatusDTO dto = new BillingStatusDTO();
        dto.setMemberId(memberId);
        dto.setHasDebt(hasDebt);
        return ResponseEntity.ok(dto);
    }

    // ENDPOINT 2: Para que ms-member (El Creador) avise que nació un socio
    @Operation(summary = "Crear un nuevo registro", description = "Crear un nuevo registro en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping("/initialize/{memberId}")
    public ResponseEntity<Void> initialize(@PathVariable Long memberId) {
        service.initializeAccount(memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}