package cl.sportmedics.ms_inventory.service;

import cl.sportmedics.ms_inventory.dto.InventoryItemRequestDTO;
import cl.sportmedics.ms_inventory.dto.InventoryItemResponseDTO;
import cl.sportmedics.ms_inventory.entity.InventoryItem;
import cl.sportmedics.ms_inventory.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository repository;

    @Override
    public InventoryItemResponseDTO create(InventoryItemRequestDTO dto) {
        log.info("Registrando nuevo artículo de inventario: {}", dto.getName());

        if (repository.findByNameAndLocation(dto.getName(), dto.getLocation()).isPresent()) {
            throw new RuntimeException("El artículo ya existe en esa ubicación.");
        }

        InventoryItem item = InventoryItem.builder()
                .name(dto.getName())
                .category(dto.getCategory().toUpperCase())
                .quantity(dto.getQuantity())
                .location(dto.getLocation())
                .active(dto.getActive())
                .build();

        InventoryItem saved = repository.save(item);
        log.info("Artículo registrado exitosamente con ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    public List<InventoryItemResponseDTO> getAll() {
        log.info("Consultando catálogo completo de inventario.");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public InventoryItemResponseDTO getById(Long id) {
        log.info("Buscando artículo con ID: {}", id);
        return repository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado."));
    }

    @Override
    public List<InventoryItemResponseDTO> getByCategory(String category) {
        log.info("Filtrando inventario por categoría: {}", category);
        return repository.findByCategory(category.toUpperCase()).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public InventoryItemResponseDTO update(Long id, InventoryItemRequestDTO dto) {
        log.info("Actualizando datos del artículo ID: {}", id);
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado."));

        item.setName(dto.getName());
        item.setCategory(dto.getCategory().toUpperCase());
        item.setQuantity(dto.getQuantity());
        item.setLocation(dto.getLocation());
        item.setActive(dto.getActive());

        log.info("Artículo ID: {} actualizado correctamente.", id);
        return mapToDTO(repository.save(item));
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando artículo ID: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Artículo no encontrado.");
        }
        repository.deleteById(id);
        log.info("Artículo ID: {} eliminado del sistema.", id);
    }

    private InventoryItemResponseDTO mapToDTO(InventoryItem item) {
        InventoryItemResponseDTO dto = new InventoryItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setLocation(item.getLocation());
        dto.setActive(item.getActive());
        return dto;
    }
}