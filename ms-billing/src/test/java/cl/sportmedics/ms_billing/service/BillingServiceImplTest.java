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

    @Test
    public void testGetAll() {
        // ARRANGE: preparar datos y mocks
        Billing billing1 = new Billing();
        billing1.setId(1L);
        Billing billing2 = new Billing();
        billing2.setId(2L);
        
        when(repository.findAll()).thenReturn(java.util.Arrays.asList(billing1, billing2));

        // ACT: ejecutar método
        java.util.List<BillingResponseDTO> response = service.getAll();

        // ASSERT: verificar resultado esperado
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals(2L, response.get(1).getId());

        // VERIFY: comprobar llamadas al mock
        verify(repository).findAll();
    }

    @Test
    public void testGetById() {
        // ARRANGE: preparar datos
        Billing billing = new Billing();
        billing.setId(5L);
        billing.setStatus("PAID");
        
        when(repository.findById(5L)).thenReturn(java.util.Optional.of(billing));

        // ACT: ejecutar método
        BillingResponseDTO response = service.getById(5L);

        // ASSERT: verificar resultado esperado
        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("PAID", response.getStatus());

        // VERIFY: comprobar llamadas al mock
        verify(repository).findById(5L);
    }

    @Test
    public void testUpdate() {
        // ARRANGE: preparar datos
        Billing existingBilling = new Billing();
        existingBilling.setId(5L);
        existingBilling.setTotalAmount(100.0);
        existingBilling.setStatus("PENDING");
        
        when(repository.findById(5L)).thenReturn(java.util.Optional.of(existingBilling));

        BillingRequestDTO request = new BillingRequestDTO();
        request.setTotalAmount(200.0);
        request.setStatus("PAID");
        request.setDueDate(LocalDate.now().plusDays(5));

        Billing updatedBilling = new Billing();
        updatedBilling.setId(5L);
        updatedBilling.setTotalAmount(200.0);
        updatedBilling.setStatus("PAID");

        when(repository.save(any(Billing.class))).thenReturn(updatedBilling);

        // ACT: ejecutar método
        BillingResponseDTO response = service.update(5L, request);

        // ASSERT: verificar resultado esperado
        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals(200.0, response.getTotalAmount());
        assertEquals("PAID", response.getStatus());

        // VERIFY: comprobar llamadas al mock
        verify(repository).findById(5L);
        verify(repository).save(any(Billing.class));
    }

    @Test
    public void testDelete() {
        // ARRANGE: preparar mock
        when(repository.existsById(5L)).thenReturn(true);
        // repository.deleteById devuelve void, no necesitamos thenReturn.

        // ACT: ejecutar método
        service.delete(5L);

        // VERIFY: comprobar llamadas al mock
        verify(repository).existsById(5L);
        verify(repository).deleteById(5L);
    }

    @Test
    public void testProcessPayment() {
        // ARRANGE: preparar mock
        Billing billing = new Billing();
        billing.setId(5L);
        billing.setTotalAmount(100.0);
        billing.setStatus("PENDING");
        billing.setPayments(new java.util.ArrayList<>()); // Inicializamos la lista de pagos
        
        when(repository.findById(5L)).thenReturn(java.util.Optional.of(billing));

        // ACT: ejecutar método
        service.processPayment(5L, 100.0, "CASH", "REF123");

        // ASSERT & VERIFY: comprobar cambios y llamadas
        verify(repository).findById(5L);
        verify(repository).save(billing); // Validamos que se guarde la factura actualizada
        
        assertEquals("PAID", billing.getStatus()); // El estado debe cambiar a pagado
        assertEquals(1, billing.getPayments().size()); // Debe existir un pago registrado
        assertEquals(100.0, billing.getPayments().get(0).getAmountPaid());
    }

    @Test
    public void testCheckDebtStatus() {
        // ARRANGE: simular que el socio tiene una boleta PENDING
        Billing billing = new Billing();
        billing.setId(10L);
        billing.setStatus("PENDING");
        
        when(repository.findByMemberId(1L)).thenReturn(java.util.Collections.singletonList(billing));

        // ACT: consultar estado de deuda
        boolean hasDebt = service.checkDebtStatus(1L);

        // ASSERT: debe ser true ya que hay una pendiente
        org.junit.jupiter.api.Assertions.assertTrue(hasDebt);

        // VERIFY: confirmar la consulta
        verify(repository).findByMemberId(1L);
    }

    @Test
    public void testInitializeAccount() {
        // ARRANGE: simular un socio nuevo que no tiene historial
        when(repository.findByMemberId(2L)).thenReturn(new java.util.ArrayList<>());

        // ACT: ejecutar método de inicialización
        service.initializeAccount(2L);

        // VERIFY: validar que buscó facturas y luego insertó una inicial
        verify(repository).findByMemberId(2L);
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
