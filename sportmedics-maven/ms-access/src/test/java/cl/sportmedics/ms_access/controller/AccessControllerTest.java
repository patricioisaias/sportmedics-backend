package cl.sportmedics.ms_access.controller;

import cl.sportmedics.ms_access.dto.AccessRequestDTO;
import cl.sportmedics.ms_access.dto.AccessResponseDTO;
import cl.sportmedics.ms_access.service.AccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(AccessController.class)
public class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccessService accessService;

    @Test
    public void testGetAllLogs() throws Exception {
        // ARRANGE: preparar datos y mocks.
        AccessResponseDTO log1 = new AccessResponseDTO();
        log1.setId(1L);
        log1.setMemberId(10L);
        log1.setGranted(true);
        
        AccessResponseDTO log2 = new AccessResponseDTO();
        log2.setId(2L);
        log2.setMemberId(20L);
        log2.setGranted(false);
        log2.setDenialReason("Deuda pendiente");
        
        List<AccessResponseDTO> logs = Arrays.asList(log1, log2);
        when(accessService.getAllAccessLogs()).thenReturn(logs);

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(get("/api/accesses")
                .contentType(MediaType.APPLICATION_JSON))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].granted").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].granted").value(false));

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(accessService).getAllAccessLogs();
        
        // Comentario QA: Si se esperaba HTTP 200 OK y se obtuvo HTTP 404, QA debería reportarlo indicando que la ruta no existe, y desarrollo debería revisar el @GetMapping del controller.
    }

    @Test
    public void testVerifyAndRegister() throws Exception {
        // ARRANGE: preparar datos y mocks.
        AccessRequestDTO requestDTO = new AccessRequestDTO();
        requestDTO.setMemberId(50L);

        AccessResponseDTO responseDTO = new AccessResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setMemberId(50L);
        responseDTO.setGranted(true);

        when(accessService.registerAccessAttempt(org.mockito.ArgumentMatchers.any(AccessRequestDTO.class)))
                .thenReturn(responseDTO);

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(post("/api/accesses/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"memberId\": 50}"))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.memberId").value(50))
                .andExpect(jsonPath("$.granted").value(true));

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(accessService).registerAccessAttempt(org.mockito.ArgumentMatchers.any(AccessRequestDTO.class));
        
        // Comentario QA: Si el request fallara por enviar un JSON vacío, retornaría un 400 Bad Request, lo cual debe ser reportado si se esperaba validación o si faltan datos obligatorios.
    }

    @Test
    public void testGetByMember() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long memberId = 99L;
        
        AccessResponseDTO log1 = new AccessResponseDTO();
        log1.setId(100L);
        log1.setMemberId(memberId);
        log1.setGranted(true);
        
        List<AccessResponseDTO> logs = Arrays.asList(log1);
        when(accessService.getLogsByMemberId(memberId)).thenReturn(logs);

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(get("/api/accesses/member/{memberId}", memberId)
                .contentType(MediaType.APPLICATION_JSON))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].memberId").value(99));

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(accessService).getLogsByMemberId(memberId);
        
        // Comentario QA: Si el endpoint retorna 200 OK con un array vacío [] cuando no hay registros, QA debe darlo por exitoso. Si devolviera 404, indicaría un problema en el desarrollo del servicio.
    }
}
