package cl.sportmedics.ms_billing.controller;

import cl.sportmedics.ms_billing.dto.BillingRequestDTO;
import cl.sportmedics.ms_billing.dto.BillingResponseDTO;
import cl.sportmedics.ms_billing.service.BillingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillingController.class)
public class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillingService billingService;

    @Test
    public void testCreate() throws Exception {
        // ARRANGE: preparar datos y mocks
        BillingResponseDTO responseDto = new BillingResponseDTO();
        responseDto.setId(1L);

        when(billingService.create(any(BillingRequestDTO.class))).thenReturn(responseDto);

        String validJson = "{\"totalAmount\": 100.0, \"dueDate\": \"2024-12-31\", \"issueDate\": \"2024-12-01\", \"subscriptionId\": 1, \"memberId\": 1, \"status\": \"PENDING\"}";

        // ACT: ejecutar método o endpoint
        mockMvc.perform(post("/api/billings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        // VERIFY: comprobar llamadas al mock
        verify(billingService).create(any(BillingRequestDTO.class));
    }

    @Test
    public void testGetAllBillings() throws Exception {
        // ARRANGE: preparar datos y mocks
        BillingResponseDTO dto1 = new BillingResponseDTO();
        dto1.setId(1L);
        BillingResponseDTO dto2 = new BillingResponseDTO();
        dto2.setId(2L);
        
        List<BillingResponseDTO> billings = Arrays.asList(dto1, dto2);
        when(billingService.getAll()).thenReturn(billings);

        // ACT: ejecutar método o endpoint
        mockMvc.perform(get("/api/billings")
                .contentType(MediaType.APPLICATION_JSON))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        // VERIFY: comprobar llamadas al mock
        verify(billingService).getAll();
    }

    @Test
    public void testGetBillingById() throws Exception {
        // ARRANGE: preparar datos y mocks
        BillingResponseDTO responseDto = new BillingResponseDTO();
        responseDto.setId(5L);
        
        when(billingService.getById(5L)).thenReturn(responseDto);

        // ACT: ejecutar método o endpoint
        mockMvc.perform(get("/api/billings/5")
                .contentType(MediaType.APPLICATION_JSON))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));

        // VERIFY: comprobar llamadas al mock
        verify(billingService).getById(5L);
    }

    @Test
    public void testUpdateBilling() throws Exception {
        // ARRANGE: preparar datos y mocks
        BillingResponseDTO responseDto = new BillingResponseDTO();
        responseDto.setId(5L);
        
        when(billingService.update(eq(5L), any(BillingRequestDTO.class))).thenReturn(responseDto);

        String validJson = "{\"totalAmount\": 150.0, \"dueDate\": \"2024-12-31\", \"issueDate\": \"2024-12-01\", \"subscriptionId\": 1, \"memberId\": 1, \"status\": \"PAID\"}";

        // ACT: ejecutar método o endpoint
        mockMvc.perform(put("/api/billings/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));

        // VERIFY: comprobar llamadas al mock
        verify(billingService).update(eq(5L), any(BillingRequestDTO.class));
    }

    @Test
    public void testDeleteBilling() throws Exception {
        // ARRANGE: preparar datos y mocks
        // No es necesario simular el retorno porque el método en el servicio es void.
        
        // ACT: ejecutar método o endpoint
        mockMvc.perform(delete("/api/billings/5")
                .contentType(MediaType.APPLICATION_JSON))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock
        verify(billingService).delete(5L);
    }

    @Test
    public void testPayBilling() throws Exception {
        // ARRANGE: preparar datos y mocks
        // Al ser un método void en el servicio, no es necesario simular retorno.
        String validJson = "{\"amount\": 100.0, \"method\": \"CREDIT_CARD\", \"reference\": \"REF123\"}";

        // ACT: ejecutar método o endpoint
        mockMvc.perform(post("/api/billings/5/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pago procesado correctamente."));

        // VERIFY: comprobar llamadas al mock
        verify(billingService).processPayment(eq(5L), eq(100.0), eq("CREDIT_CARD"), eq("REF123"));
    }

    @Test
    public void testGetStatus() throws Exception {
        // ARRANGE: preparar datos y mocks
        when(billingService.checkDebtStatus(1L)).thenReturn(true);

        // ACT: ejecutar método o endpoint
        mockMvc.perform(get("/api/billings/status/1")
                .contentType(MediaType.APPLICATION_JSON))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.hasDebt").value(true));

        // VERIFY: comprobar llamadas al mock
        verify(billingService).checkDebtStatus(1L);
    }

    @Test
    public void testInitializeBilling() throws Exception {
        // ARRANGE: preparar datos y mocks
        // Al ser un método void en el servicio, no es necesario un when(...) return.

        // ACT: ejecutar método o endpoint
        mockMvc.perform(post("/api/billings/initialize/1")
                .contentType(MediaType.APPLICATION_JSON))
                
        // ASSERT: verificar resultado esperado
                .andExpect(status().isCreated());

        // VERIFY: comprobar llamadas al mock
        verify(billingService).initializeAccount(1L);
    }
}
/*
 Caso hipotético de falla para QA:
 Se esperaba: HTTP 201 Created
 Se obtuvo: HTTP 400 Bad Request
 
 Cómo reportarlo (QA): Informar que, al enviar un payload al endpoint POST `/api/billings`, el servicio arroja un Bad Request (400) en lugar de un Created (201). Se debe adjuntar el payload enviado para ayudar a la investigación.
 Qué revisar (Desarrollo): Desarrollo debe revisar las anotaciones `@Valid` en `BillingRequestDTO` (ej. `@NotNull`, `@NotBlank`) para asegurarse de que las reglas de negocio en la API coinciden con lo que QA (o el FrontEnd) están enviando.
*/
