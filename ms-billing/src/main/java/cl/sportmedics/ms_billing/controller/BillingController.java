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
@Tag(name = "Facturación", description = "Operaciones relacionadas con Facturación")
@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    @Operation(summary = "Crear una factura", description = "Crea y guarda una factura en el sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping
    public ResponseEntity<BillingResponseDTO> create(@Valid @RequestBody BillingRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los facturas", description = "Retorna una lista de todos los facturas registrados")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<BillingResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Obtener una factura por ID", description = "Retorna los detalles de una factura específico")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Actualizar una factura", description = "Actualiza los datos de una factura existente por ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BillingRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar una factura", description = "Elimina una factura del sistema por ID")
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

    @Operation(summary = "Crear una factura", description = "Crea y guarda una factura en el sistema")
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
    @Operation(summary = "Obtener facturas por miembro", description = "Retorna una lista de facturas asociados a un miembro")
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
    @Operation(summary = "Crear una factura", description = "Crea y guarda una factura en el sistema")
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