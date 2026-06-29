package cl.sportmedics.ms_inventory.service;

import cl.sportmedics.ms_inventory.dto.InventoryItemRequestDTO;
import cl.sportmedics.ms_inventory.dto.InventoryItemResponseDTO;
import cl.sportmedics.ms_inventory.entity.InventoryItem;
import cl.sportmedics.ms_inventory.repository.InventoryItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryItemRepository repository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void testCreate() {
        // ARRANGE: preparar datos y mocks.
        InventoryItemRequestDTO request = new InventoryItemRequestDTO();
        request.setName("Pesas 10kg");
        request.setCategory("WEIGHTS");
        request.setQuantity(20);
        request.setLocation("Bodega 1");
        request.setActive(true);

        InventoryItem savedItem = InventoryItem.builder()
                .id(1L)
                .name("Pesas 10kg")
                .category("WEIGHTS")
                .quantity(20)
                .location("Bodega 1")
                .active(true)
                .build();

        Mockito.when(repository.findByNameAndLocation(request.getName(), request.getLocation())).thenReturn(Optional.empty());
        Mockito.when(repository.save(any(InventoryItem.class))).thenReturn(savedItem);

        // ACT: ejecutar método o endpoint.
        InventoryItemResponseDTO response = inventoryService.create(request);

        // ASSERT: verificar resultado esperado.
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Pesas 10kg", response.getName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).findByNameAndLocation(request.getName(), request.getLocation());
        Mockito.verify(repository, Mockito.times(1)).save(any(InventoryItem.class));

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: Creación exitosa
        // Se obtuvo: RuntimeException("El artículo ya existe en esa ubicación.")
        // Explicación: QA intenta crear un artículo que ya existe en la misma ubicación. El sistema rechaza la solicitud. Desarrollo debería validar que este error devuelva un código HTTP 409 Conflict.
    }

    @Test
    void testGetAll() {
        // ARRANGE: preparar datos y mocks.
        InventoryItem item1 = InventoryItem.builder().id(1L).name("Cinta").category("MACHINES").build();
        InventoryItem item2 = InventoryItem.builder().id(2L).name("Mancuernas").category("WEIGHTS").build();

        Mockito.when(repository.findAll()).thenReturn(java.util.List.of(item1, item2));

        // ACT: ejecutar método o endpoint.
        java.util.List<InventoryItemResponseDTO> responses = inventoryService.getAll();

        // ASSERT: verificar resultado esperado.
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Cinta", responses.get(0).getName());
        assertEquals("Mancuernas", responses.get(1).getName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).findAll();

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: Una lista con 2 DTOs mapeados
        // Se obtuvo: NullPointerException
        // Explicación: QA reporta un fallo crítico al listar. Desarrollo debe revisar si alguna entidad guardada en BD tiene propiedades nulas y el método mapToDTO no las está manejando correctamente.
    }

    @Test
    void testGetById() {
        // ARRANGE: preparar datos y mocks.
        Long id = 1L;
        InventoryItem item = InventoryItem.builder().id(id).name("Mancuernas").category("WEIGHTS").build();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(item));

        // ACT: ejecutar método o endpoint.
        InventoryItemResponseDTO response = inventoryService.getById(id);

        // ASSERT: verificar resultado esperado.
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Mancuernas", response.getName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).findById(id);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: El DTO mapeado del artículo
        // Se obtuvo: RuntimeException("Artículo no encontrado.")
        // Explicación: QA busca un artículo que fue borrado recientemente. El sistema debe lanzar excepción, pero desarrollo debería cambiarlo por una que mapee a un 404 estructurado y no a un error genérico 500.
    }

    @Test
    void testGetByCategory() {
        // ARRANGE: preparar datos y mocks.
        String category = "MACHINES";
        InventoryItem item = InventoryItem.builder().id(1L).name("Cinta").category(category).build();

        Mockito.when(repository.findByCategory(category)).thenReturn(java.util.List.of(item));

        // ACT: ejecutar método o endpoint.
        java.util.List<InventoryItemResponseDTO> responses = inventoryService.getByCategory(category);

        // ASSERT: verificar resultado esperado.
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Cinta", responses.get(0).getName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).findByCategory(category);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: Una lista filtrada por la categoría
        // Se obtuvo: NullPointerException
        // Explicación: QA intenta buscar y la aplicación falla. Desarrollo debe revisar si en BD o repositorio existe posibilidad de que findByCategory retorne un valor 'null' en vez de una lista vacía.
    }

    @Test
    void testUpdate() {
        // ARRANGE: preparar datos y mocks.
        Long id = 1L;
        InventoryItemRequestDTO request = new InventoryItemRequestDTO();
        request.setName("Cinta Actualizada");
        request.setCategory("MACHINES");
        request.setQuantity(15);
        request.setLocation("Piso 2");
        request.setActive(true);

        InventoryItem existingItem = InventoryItem.builder().id(id).name("Cinta").category("MACHINES").quantity(5).location("Piso 1").active(true).build();
        InventoryItem updatedItem = InventoryItem.builder().id(id).name("Cinta Actualizada").category("MACHINES").quantity(15).location("Piso 2").active(true).build();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingItem));
        Mockito.when(repository.save(any(InventoryItem.class))).thenReturn(updatedItem);

        // ACT: ejecutar método o endpoint.
        InventoryItemResponseDTO response = inventoryService.update(id, request);

        // ASSERT: verificar resultado esperado.
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Cinta Actualizada", response.getName());
        assertEquals(15, response.getQuantity());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Mockito.verify(repository, Mockito.times(1)).save(any(InventoryItem.class));

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: DTO del artículo actualizado.
        // Se obtuvo: RuntimeException("Artículo no encontrado.")
        // Explicación: QA intenta actualizar un artículo que fue eliminado. El sistema bloquea la actualización. Desarrollo debe asegurarse de que el API retorne HTTP 404 y no un error interno de servidor (500).
    }

    @Test
    void testDelete() {
        // ARRANGE: preparar datos y mocks.
        Long id = 1L;
        Mockito.when(repository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(id);

        // ACT: ejecutar método o endpoint.
        inventoryService.delete(id);

        // ASSERT: verificar resultado esperado.
        // Al ser de tipo void, si se ejecuta sin lanzar excepción significa que el flujo fue exitoso.

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(repository, Mockito.times(1)).existsById(id);
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: Eliminación exitosa sin excepciones.
        // Se obtuvo: RuntimeException("Artículo no encontrado.")
        // Explicación: QA presiona eliminar un par de veces rápido y la segunda falla porque ya no existe. El equipo de desarrollo debe revisar si el front-end deshabilita el botón adecuadamente.
    }
}
