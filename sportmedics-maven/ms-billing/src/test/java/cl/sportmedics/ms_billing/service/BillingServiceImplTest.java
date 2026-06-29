package cl.sportmedics.ms_billing.service;

import cl.sportmedics.ms_billing.dto.BillingRequestDTO;
import cl.sportmedics.ms_billing.dto.BillingResponseDTO;
import cl.sportmedics.ms_billing.entity.Billing;
import cl.sportmedics.ms_billing.repository.BillingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillingServiceImplTest {

    @Mock
    private BillingRepository repository;

    @InjectMocks
    private BillingServiceImpl service;

    @Test
    public void testCreate() {
        // ARRANGE: preparar datos y mocks
        BillingRequestDTO request = new BillingRequestDTO();
        request.setMemberId(1L);
        request.setSubscriptionId(1L);
        request.setIssueDate(LocalDate.now());
        request.setDueDate(LocalDate.now().plusDays(30));
        request.setTotalAmount(100.0);
        request.setStatus("PENDING");

        Billing savedBilling = new Billing();
        savedBilling.setId(10L);
        savedBilling.setMemberId(1L);
        savedBilling.setSubscriptionId(1L);
        savedBilling.setTotalAmount(100.0);
        savedBilling.setStatus("PENDING");

        when(repository.save(any(Billing.class))).thenReturn(savedBilling);

        // ACT: ejecutar método o endpoint
        BillingResponseDTO response = service.create(request);

        // ASSERT: verificar resultado esperado
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("PENDING", response.getStatus());

        // VERIFY: comprobar llamadas al mock
        verify(repository).save(any(Billing.class));
    }
}
/*
 Caso hipotético de falla para QA:
 Se esperaba: Un objeto BillingResponseDTO con ID=10L
 Se obtuvo: NullPointerException
 
 Cómo reportarlo (QA): El test unitario arroja NPE al guardar.
 Qué revisar (Desarrollo): Revisar si el mapToDTO falla al recibir parámetros nulos desde la base de datos o si un objeto relacionado viene vacío.
*/
