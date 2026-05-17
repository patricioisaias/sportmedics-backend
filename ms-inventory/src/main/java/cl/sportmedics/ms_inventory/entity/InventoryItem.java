package cl.sportmedics.ms_inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category; // Ej: MACHINES, WEIGHTS, ACCESSORIES, SUPPLEMENTS

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Boolean active;
}