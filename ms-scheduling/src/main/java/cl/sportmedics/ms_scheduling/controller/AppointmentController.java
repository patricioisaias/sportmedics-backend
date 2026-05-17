package cl.sportmedics.ms_scheduling.controller;

import cl.sportmedics.ms_scheduling.dto.AppointmentRequestDTO;
import cl.sportmedics.ms_scheduling.dto.AppointmentResponseDTO;
import cl.sportmedics.ms_scheduling.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/scheduling")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(@Valid @RequestBody AppointmentRequestDTO dto) {
        log.info("Petición POST recibida en /api/scheduling");
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(service.getByMemberId(memberId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        log.info("Petición PATCH recibida en /api/scheduling/{}/status", id);
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}