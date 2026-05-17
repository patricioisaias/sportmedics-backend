package cl.sportmedics.ms_billing.controller;


import cl.sportmedics.ms_billing.dto.BillingRequestDTO;
import cl.sportmedics.ms_billing.dto.BillingResponseDTO;
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
@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;

    @PostMapping
    public ResponseEntity<BillingResponseDTO> create(@Valid @RequestBody BillingRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BillingResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BillingRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<Map<String, String>> pay(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Double amount = Double.valueOf(payload.get("amount").toString());
        String method = payload.get("method").toString();
        String ref = payload.get("reference") != null ? payload.get("reference").toString() : null;

        service.processPayment(id, amount, method, ref);
        return ResponseEntity.ok(Map.of("message", "Pago procesado correctamente."));
    }
}