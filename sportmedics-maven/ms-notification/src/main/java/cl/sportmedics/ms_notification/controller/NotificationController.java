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
@Tag(name = "Notification", description = "Operaciones relacionadas con Notification")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @Operation(summary = "Crear un nuevo registro", description = "Crear un nuevo registro en el sistema")

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

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "Operación exitosa"),

        @ApiResponse(responseCode = "400", description = "Petición inválida"),

        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    @Operation(summary = "Obtener registros", description = "Obtener registros en el sistema")

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