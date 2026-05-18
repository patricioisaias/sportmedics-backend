package cl.sportmedics.ms_auth.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class AuthRegisterDTO {
    @NotBlank(message = "El nombre de usuario (email) es obligatorio.")
    @Email(message = "Debe ser un correo válido.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    @NotBlank(message = "El rol es obligatorio.")
    private String role;
}