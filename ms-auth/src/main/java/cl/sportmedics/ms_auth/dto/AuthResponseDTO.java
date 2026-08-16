package cl.sportmedics.ms_auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
    private String username;
    private String role;
    private String token; // Aquí viajará el JWT
    private String message;
}