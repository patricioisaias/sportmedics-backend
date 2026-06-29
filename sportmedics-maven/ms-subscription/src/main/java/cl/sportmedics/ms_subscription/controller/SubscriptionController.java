package cl.sportmedics.ms_subscription.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.sportmedics.ms_subscription.dto.SubscriptionRequestDTO;
import cl.sportmedics.ms_subscription.dto.SubscriptionResponseDTO;
import cl.sportmedics.ms_subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Subscription", description = "Operaciones relacionadas con Subscription")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @Operation(summary = "Realizar operación", description = "Endpoint para realizar operaciones en Subscription")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> create(@Valid @RequestBody SubscriptionRequestDTO dto) {
        log.info("PeticiÃƒÂ³n POST recibida en /api/subscriptions");
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Realizar operación", description = "Endpoint para realizar operaciones en Subscription")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getAll() {
        log.info("PeticiÃƒÂ³n GET recibida en /api/subscriptions");
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Realizar operación", description = "Endpoint para realizar operaciones en Subscription")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getById(@PathVariable Long id) {
        log.info("PeticiÃƒÂ³n GET recibida en /api/subscriptions/{}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Realizar operación", description = "Endpoint para realizar operaciones en Subscription")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody SubscriptionRequestDTO dto) {
        log.info("PeticiÃƒÂ³n PUT recibida en /api/subscriptions/{}", id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Realizar operación", description = "Endpoint para realizar operaciones en Subscription")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("PeticiÃƒÂ³n DELETE recibida en /api/subscriptions/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
