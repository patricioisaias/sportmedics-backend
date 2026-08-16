package cl.sportmedics.ms_access.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessRequestDTO {
    @NotNull(message = "El ID del miembro es obligatorio para registrar el acceso.")
    private Long memberId;
}