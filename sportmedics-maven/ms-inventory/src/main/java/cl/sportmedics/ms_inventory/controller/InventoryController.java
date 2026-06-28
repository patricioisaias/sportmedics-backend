package cl.sportmedics.ms_inventory.controller;

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
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> create(@Valid @RequestBody InventoryItemRequestDTO dto) {
        log.info("Petición POST recibida en /api/inventory");
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemResponseDTO>> getAll() {
        log.info("Petición GET recibida en /api/inventory");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> getById(@PathVariable Long id) {
        log.info("Petición GET recibida en /api/inventory/{}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<InventoryItemResponseDTO>> getByCategory(@PathVariable String category) {
        log.info("Petición GET recibida en /api/inventory/category/{}", category);
        return ResponseEntity.ok(service.getByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody InventoryItemRequestDTO dto) {
        log.info("Petición PUT recibida en /api/inventory/{}", id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Petición DELETE recibida en /api/inventory/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}