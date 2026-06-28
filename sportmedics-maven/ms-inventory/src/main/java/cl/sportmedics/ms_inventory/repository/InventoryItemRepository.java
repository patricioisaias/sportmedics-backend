package cl.sportmedics.ms_inventory.repository;

import cl.sportmedics.ms_inventory.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByNameAndLocation(String name, String location);

    List<InventoryItem> findByCategory(String category);
}