package cl.sportmedics.ms_notification.controller;

import cl.sportmedics.ms_notification.dto.NotificationRequestDTO;
import cl.sportmedics.ms_notification.dto.NotificationResponseDTO;
import cl.sportmedics.ms_notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> send(@Valid @RequestBody NotificationRequestDTO dto) {
        log.info("Petición POST recibida en /api/notifications/send");
        return new ResponseEntity<>(service.sendNotification(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    @GetMapping("/email")
    public ResponseEntity<List<NotificationResponseDTO>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.getNotificationsByEmail(email));
    }
}