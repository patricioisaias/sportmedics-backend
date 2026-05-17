package cl.sportmedics.ms_billing.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BillingRequestDTO {
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

    private String phone;

    private Boolean active = true;
}