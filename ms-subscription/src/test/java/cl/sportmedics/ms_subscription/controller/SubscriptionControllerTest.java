package cl.sportmedics.ms_subscription.controller;

import cl.sportmedics.ms_subscription.dto.SubscriptionRequestDTO;
import cl.sportmedics.ms_subscription.dto.SubscriptionResponseDTO;
import cl.sportmedics.ms_subscription.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreate() throws Exception {
        // ARRANGE: Preparar DTO de petición (Request)
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setName("PRO");
        requestDTO.setPrice(99.99);
        requestDTO.setDurationMonths(12);
        requestDTO.setActive(true);

        // Preparar DTO de respuesta esperado
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("PRO");
        responseDTO.setPrice(99.99);
        responseDTO.setDurationMonths(12);
        responseDTO.setActive(true);

        when(service.create(any(SubscriptionRequestDTO.class))).thenReturn(responseDTO);

        // ACT & ASSERT: Simular la petición HTTP POST
        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("PRO"));

        // VERIFY: Comprobar que el Controller llamó al Service
        verify(service).create(any(SubscriptionRequestDTO.class));
        
        // Comentario QA: Si se omiten campos obligatorios (como 'price'), el controlador lanzará 400 Bad Request por el @Valid.
    }

    @Test
    public void testGetAll() throws Exception {
        // ARRANGE: Preparar lista de suscripciones mockeadas
        SubscriptionResponseDTO sub1 = new SubscriptionResponseDTO();
        sub1.setId(1L);
        sub1.setName("BASIC");

        SubscriptionResponseDTO sub2 = new SubscriptionResponseDTO();
        sub2.setId(2L);
        sub2.setName("PRO");

        List<SubscriptionResponseDTO> list = Arrays.asList(sub1, sub2);

        when(service.getAll()).thenReturn(list);

        // ACT & ASSERT: Simular petición HTTP GET
        mockMvc.perform(get("/api/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("BASIC"))
                .andExpect(jsonPath("$[1].name").value("PRO"));

        // VERIFY: Comprobar que se invocó la búsqueda en el servicio
        verify(service).getAll();
    }

    @Test
    public void testGetById() throws Exception {
        // ARRANGE: Preparar el ID y su respectiva respuesta mockeada
        Long id = 1L;
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO();
        responseDTO.setId(id);
        responseDTO.setName("PRO");

        when(service.getById(id)).thenReturn(responseDTO);

        // ACT & ASSERT: Simular petición HTTP GET con path variable
        mockMvc.perform(get("/api/subscriptions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("PRO"));

        // VERIFY: Asegurar que se usó el servicio para buscar dicho ID
        verify(service).getById(id);
        
        // Comentario QA: Si el ID no existe en la BD, la prueba de integración mostrará que se arroja un 404/500 según el GlobalExceptionHandler.
    }

    @Test
    public void testUpdate() throws Exception {
        // ARRANGE: Preparar datos para actualizar
        Long id = 1L;
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setName("PREMIUM");
        requestDTO.setPrice(149.99);
        requestDTO.setDurationMonths(24);
        requestDTO.setActive(true);

        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO();
        responseDTO.setId(id);
        responseDTO.setName("PREMIUM");
        responseDTO.setPrice(149.99);
        responseDTO.setDurationMonths(24);
        responseDTO.setActive(true);

        when(service.update(eq(id), any(SubscriptionRequestDTO.class))).thenReturn(responseDTO);

        // ACT & ASSERT: Simular petición HTTP PUT
        mockMvc.perform(put("/api/subscriptions/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("PREMIUM"));

        // VERIFY: Comprobar que la capa service fue invocada con los parámetros exactos
        verify(service).update(eq(id), any(SubscriptionRequestDTO.class));
    }

    @Test
    public void testDelete() throws Exception {
        // ARRANGE: Definir el ID a eliminar
        Long id = 1L;

        // ACT & ASSERT: Simular petición HTTP DELETE
        mockMvc.perform(delete("/api/subscriptions/{id}", id))
                .andExpect(status().isNoContent()); // Esperamos un 204 No Content

        // VERIFY: Comprobar que se llamó el método borrar en el Service
        verify(service).delete(id);
    }
}
