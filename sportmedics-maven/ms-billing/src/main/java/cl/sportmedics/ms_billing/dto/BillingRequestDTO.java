package cl.sportmedics.ms_billing.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BillingRequestDTO {

    @NotNull(message = "El ID del miembro es obligatorio.")
    private Long memberId;

    @NotNull(message = "El ID de la suscripción es obligatorio.")
    private Long subscriptionId;

    @NotNull(message = "La fecha de emisión es obligatoria.")
    private LocalDate issueDate;

    @NotNull(message = "La fecha de vencimiento es obligatoria.")
    private LocalDate dueDate;

    @NotNull(message = "El monto total es obligatorio.")
    @Positive(message = "El monto debe ser mayor a cero.")
    private Double totalAmount;

    @NotBlank(message = "El estado es obligatorio (ej: PENDING, PAID).")
    private String status;
}