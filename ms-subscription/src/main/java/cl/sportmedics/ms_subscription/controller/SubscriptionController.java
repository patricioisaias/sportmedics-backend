package cl.sportmedics.ms_subscription.controller;

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
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> create(@Valid @RequestBody SubscriptionRequestDTO dto) {
        log.info("Petición POST recibida en /api/subscriptions");
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getAll() {
        log.info("Petición GET recibida en /api/subscriptions");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getById(@PathVariable Long id) {
        log.info("Petición GET recibida en /api/subscriptions/{}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody SubscriptionRequestDTO dto) {
        log.info("Petición PUT recibida en /api/subscriptions/{}", id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Petición DELETE recibida en /api/subscriptions/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}