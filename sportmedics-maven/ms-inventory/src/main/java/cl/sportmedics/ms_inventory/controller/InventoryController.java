package cl.sportmedics.ms_inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.sportmedics.ms_inventory.dto.InventoryItemRequestDTO;
import cl.sportmedics.ms_inventory.dto.InventoryItemResponseDTO;
import cl.sportmedics.ms_inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Inventario", description = "Operaciones relacionadas con Inventario")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @Operation(summary = "Crear un artículo del inventario", description = "Crea y guarda un artículo del inventario en el sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> create(@Valid @RequestBody InventoryItemRequestDTO dto) {
        log.info("Petición POST recibida en /api/inventory");
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los artículos", description = "Retorna una lista de todos los artículos registrados")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<InventoryItemResponseDTO>> getAll() {
        log.info("Petición GET recibida en /api/inventory");
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Obtener un artículo del inventario por ID", description = "Retorna los detalles de un artículo del inventario específico")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> getById(@PathVariable Long id) {
        log.info("Petición GET recibida en /api/inventory/{}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Obtener un artículo del inventario por ID", description = "Retorna los detalles de un artículo del inventario específico")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<InventoryItemResponseDTO>> getByCategory(@PathVariable String category) {
        log.info("Petición GET recibida en /api/inventory/category/{}", category);
        return ResponseEntity.ok(service.getByCategory(category));
    }

    @Operation(summary = "Actualizar un artículo del inventario", description = "Actualiza los datos de un artículo del inventario existente por ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody InventoryItemRequestDTO dto) {
        log.info("Petición PUT recibida en /api/inventory/{}", id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar un artículo del inventario", description = "Elimina un artículo del inventario del sistema por ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Petición DELETE recibida en /api/inventory/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}