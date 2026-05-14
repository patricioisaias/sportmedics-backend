package cl.sportmedics.ms_subscription.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequestDTO {
    @NotBlank(message = "El nombre del plan no puede estar vacío")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double price;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración mínima es de 1 mes")
    private Integer durationMonths;

    private Boolean active = true;
}
