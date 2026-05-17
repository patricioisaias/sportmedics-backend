package cl.sportmedics.ms_employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmployeeRequestDTO {
    @NotBlank(message = "El RUT es obligatorio.")
    @Pattern(regexp = "^[0-9]+-[0-9Kk]{1}$", message = "Formato de RUT inválido (ej: 12345678-9).")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio.")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio.")
    private String lastName;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "Debe proporcionar un correo electrónico válido.")
    private String email;

    @NotBlank(message = "El rol del empleado es obligatorio (ej: TEACHER, ADMIN).")
    private String role;

    private Boolean active = true;
}

// EmployeeResponseDTO.java (misma estructura pero solo con los campos para responder, sin validaciones JSR).