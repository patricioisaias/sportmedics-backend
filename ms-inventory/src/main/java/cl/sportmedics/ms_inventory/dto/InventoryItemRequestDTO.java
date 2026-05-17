package cl.sportmedics.ms_inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryItemRequestDTO {
    @NotBlank(message = "El nombre del artículo es obligatorio.")
    private String name;

    @NotBlank(message = "La categoría es obligatoria (ej: MACHINES, WEIGHTS).")
    private String category;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 0, message = "La cantidad no puede ser negativa.")
    private Integer quantity;

    @NotBlank(message = "La ubicación del artículo es obligatoria.")
    private String location;

    private Boolean active = true;
}