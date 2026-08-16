package cl.sportmedics.ms_notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


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
@Tag(name = "Notificaciones", description = "Operaciones relacionadas con Notificaciones")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @Operation(summary = "Crear una notificación", description = "Crea y guarda una notificación en el sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> send(@Valid @RequestBody NotificationRequestDTO dto) {
        log.info("Petición POST recibida en /api/notifications/send");
        return new ResponseEntity<>(service.sendNotification(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los notificaciones", description = "Retorna una lista de todos los notificaciones registrados")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    @Operation(summary = "Obtener todos los notificaciones", description = "Retorna una lista de todos los notificaciones registrados")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición inválida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
    })
    @GetMapping("/email")
    public ResponseEntity<List<NotificationResponseDTO>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.getNotificationsByEmail(email));
    }
}