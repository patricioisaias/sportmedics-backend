package cl.sportmedics.ms_inventory.dto;

import lombok.Data;

@Data
public class InventoryItemResponseDTO {
    private Long id;
    private String name;
    private String category;
    private Integer quantity;
    private String location;
    private Boolean active;
}