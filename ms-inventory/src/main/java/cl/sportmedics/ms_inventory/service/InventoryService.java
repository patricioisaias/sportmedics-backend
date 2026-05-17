package cl.sportmedics.ms_inventory.service;

import cl.sportmedics.ms_inventory.dto.InventoryItemRequestDTO;
import cl.sportmedics.ms_inventory.dto.InventoryItemResponseDTO;
import java.util.List;

public interface InventoryService {
    InventoryItemResponseDTO create(InventoryItemRequestDTO dto);

    List<InventoryItemResponseDTO> getAll();

    InventoryItemResponseDTO getById(Long id);

    List<InventoryItemResponseDTO> getByCategory(String category);

    InventoryItemResponseDTO update(Long id, InventoryItemRequestDTO dto);

    void delete(Long id);
}