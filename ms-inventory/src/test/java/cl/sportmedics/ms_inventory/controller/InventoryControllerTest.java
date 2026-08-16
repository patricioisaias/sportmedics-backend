package cl.sportmedics.ms_inventory.controller;

import cl.sportmedics.ms_inventory.dto.InventoryItemRequestDTO;
import cl.sportmedics.ms_inventory.dto.InventoryItemResponseDTO;
import cl.sportmedics.ms_inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        // ARRANGE: preparar datos y mocks.
        InventoryItemRequestDTO request = new InventoryItemRequestDTO();
        request.setName("Cinta de Correr");
        request.setCategory("MACHINES");
        request.setQuantity(5);
        request.setLocation("Piso 1");
        request.setActive(true);

        InventoryItemResponseDTO response = new InventoryItemResponseDTO();
        response.setId(1L);
        response.setName("Cinta de Correr");

        Mockito.when(inventoryService.create(any(InventoryItemRequestDTO.class))).thenReturn(response);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(post("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Cinta de Correr"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(inventoryService, Mockito.times(1)).create(any(InventoryItemRequestDTO.class));
        
        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 201 Created
        // Se obtuvo: HTTP 400 Bad Request
        // Explicación: QA debería reportar que al enviar datos válidos, la API rechaza la solicitud. Desarrollo debería revisar las validaciones @Valid en el DTO o si faltó algún campo obligatorio.
    }

    @Test
    void testGetAll() throws Exception {
        // ARRANGE: preparar datos y mocks.
        InventoryItemResponseDTO item1 = new InventoryItemResponseDTO();
        item1.setId(1L);
        item1.setName("Articulo 1");

        InventoryItemResponseDTO item2 = new InventoryItemResponseDTO();
        item2.setId(2L);
        item2.setName("Articulo 2");

        Mockito.when(inventoryService.getAll()).thenReturn(List.of(item1, item2));

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(get("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Articulo 1"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(inventoryService, Mockito.times(1)).getAll();

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 200 OK con una lista
        // Se obtuvo: HTTP 500 Internal Server Error
        // Explicación: QA debería reportar falla al intentar obtener los datos. Desarrollo debería revisar posibles problemas de nulos al mapear las entidades a DTO.
    }

    @Test
    void testGetById() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long id = 1L;
        InventoryItemResponseDTO item = new InventoryItemResponseDTO();
        item.setId(id);
        item.setName("Mancuernas");

        Mockito.when(inventoryService.getById(id)).thenReturn(item);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(get("/api/inventory/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Mancuernas"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(inventoryService, Mockito.times(1)).getById(id);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 200 OK
        // Se obtuvo: HTTP 404 Not Found
        // Explicación: QA prueba con un ID que sabe que existe, pero recibe 404. Desarrollo debe revisar si el Service no recupera correctamente la información de la base de datos.
    }

    @Test
    void testGetByCategory() throws Exception {
        // ARRANGE: preparar datos y mocks.
        String category = "WEIGHTS";
        InventoryItemResponseDTO item1 = new InventoryItemResponseDTO();
        item1.setId(1L);
        item1.setCategory(category);
        
        InventoryItemResponseDTO item2 = new InventoryItemResponseDTO();
        item2.setId(2L);
        item2.setCategory(category);

        Mockito.when(inventoryService.getByCategory(category)).thenReturn(List.of(item1, item2));

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(get("/api/inventory/category/{category}", category)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].category").value(category));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(inventoryService, Mockito.times(1)).getByCategory(category);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 200 OK con una lista de ítems.
        // Se obtuvo: HTTP 400 Bad Request
        // Explicación: QA busca una categoría pero el sistema da error de request inválido. Desarrollo debe revisar si se agregó alguna validación @Valid a la variable de ruta (PathVariable) que esté fallando.
    }

    @Test
    void testUpdate() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long id = 1L;
        InventoryItemRequestDTO request = new InventoryItemRequestDTO();
        request.setName("Cinta de Correr Actualizada");
        request.setCategory("MACHINES");
        request.setQuantity(10);
        request.setLocation("Piso 2");
        request.setActive(true);

        InventoryItemResponseDTO response = new InventoryItemResponseDTO();
        response.setId(id);
        response.setName("Cinta de Correr Actualizada");
        response.setQuantity(10);

        Mockito.when(inventoryService.update(org.mockito.ArgumentMatchers.eq(id), any(InventoryItemRequestDTO.class))).thenReturn(response);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(put("/api/inventory/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cinta de Correr Actualizada"))
                .andExpect(jsonPath("$.quantity").value(10));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(inventoryService, Mockito.times(1)).update(org.mockito.ArgumentMatchers.eq(id), any(InventoryItemRequestDTO.class));

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 200 OK con los datos actualizados
        // Se obtuvo: HTTP 404 Not Found
        // Explicación: QA intenta actualizar un ID que sí existe, pero recibe un 404. Desarrollo debe revisar si el Service falla al buscar la entidad o si se está enviando un ID erróneo.
    }

    @Test
    void testDelete() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long id = 1L;
        Mockito.doNothing().when(inventoryService).delete(id);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(delete("/api/inventory/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(inventoryService, Mockito.times(1)).delete(id);

        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 204 No Content
        // Se obtuvo: HTTP 500 Internal Server Error
        // Explicación: QA reporta que al eliminar falla el servidor. Desarrollo debería revisar si hay una restricción de llave foránea en la BD.
    }
}
