package cl.sportmedics.ms_access.controller;

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
@RestController
@RequestMapping("/api/accesses")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService service;

    @PostMapping("/verify")
    public ResponseEntity<AccessResponseDTO> verifyAndRegister(@Valid @RequestBody AccessRequestDTO dto) {
        log.info("Petición POST recibida en /api/accesses/verify");
        return new ResponseEntity<>(service.registerAccessAttempt(dto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccessResponseDTO>> getAllLogs() {
        return ResponseEntity.ok(service.getAllAccessLogs());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<AccessResponseDTO>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(service.getLogsByMemberId(memberId));
    }
}