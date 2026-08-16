package cl.sportmedics.ms_subscription.service;

import cl.sportmedics.ms_subscription.dto.SubscriptionRequestDTO;
import cl.sportmedics.ms_subscription.dto.SubscriptionResponseDTO;
import cl.sportmedics.ms_subscription.entity.Subscription;
import cl.sportmedics.ms_subscription.repository.SubscriptionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository repository;

    @InjectMocks
    private SubscriptionServiceImpl service;

    @Test
    public void testGetAll() {
        // ARRANGE: preparar datos y mocks.
        Subscription sub1 = Subscription.builder()
                .id(1L)
                .name("BASIC")
                .price(9.99)
                .durationMonths(1)
                .active(true)
                .build();
        
        Subscription sub2 = Subscription.builder()
                .id(2L)
                .name("PRO")
                .price(99.99)
                .durationMonths(12)
                .active(true)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(sub1, sub2));

        // ACT: ejecutar método a probar.
        List<SubscriptionResponseDTO> result = service.getAll();

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("BASIC", result.get(0).getName());
        assertEquals(9.99, result.get(0).getPrice());
        assertEquals("PRO", result.get(1).getName());
        assertEquals(12, result.get(1).getDurationMonths());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findAll();

        // Comentario QA: Validar que la transformación de Entity a DTO mantenga la integridad de los datos numéricos y booleanos.
    }

    @Test
    public void testGetById() {
        // ARRANGE: preparar datos y mocks.
        Long subId = 5L;
        Subscription sub = Subscription.builder()
                .id(subId)
                .name("ELITE")
                .price(199.99)
                .durationMonths(24)
                .active(true)
                .build();

        when(repository.findById(subId)).thenReturn(java.util.Optional.of(sub));

        // ACT: ejecutar método a probar.
        SubscriptionResponseDTO result = service.getById(subId);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("ELITE", result.getName());
        assertEquals(199.99, result.getPrice());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findById(subId);

        // Comentario QA: Si se pasa un ID que no existe, el servicio lanzará un RuntimeException con "Plan no encontrado."
    }

    @Test
    public void testCreate_Success() {
        // ARRANGE: preparar datos y mocks.
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setName("NEW PLAN");
        requestDTO.setPrice(49.99);
        requestDTO.setDurationMonths(6);
        requestDTO.setActive(true);

        when(repository.findByName("NEW PLAN")).thenReturn(java.util.Optional.empty());

        Subscription savedSub = Subscription.builder()
                .id(10L)
                .name("NEW PLAN")
                .price(49.99)
                .durationMonths(6)
                .active(true)
                .build();

        when(repository.save(org.mockito.ArgumentMatchers.any(Subscription.class))).thenReturn(savedSub);

        // ACT: ejecutar método a probar.
        SubscriptionResponseDTO result = service.create(requestDTO);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("NEW PLAN", result.getName());
        assertEquals(49.99, result.getPrice());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findByName("NEW PLAN");
        verify(repository).save(org.mockito.ArgumentMatchers.any(Subscription.class));

        // Comentario QA: Validar escenarios de fallo donde el nombre ya existe, el servicio debe lanzar una excepción "El plan ya existe en el sistema."
    }

    @Test
    public void testUpdate() {
        // ARRANGE: preparar datos y mocks.
        Long subId = 5L;
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setName("PRO UPDATE");
        requestDTO.setPrice(129.99);
        requestDTO.setDurationMonths(12);
        requestDTO.setActive(true);

        Subscription existingSub = Subscription.builder()
                .id(subId)
                .name("PRO")
                .price(99.99)
                .durationMonths(12)
                .active(false)
                .build();

        // El servicio primero busca
        when(repository.findById(subId)).thenReturn(java.util.Optional.of(existingSub));

        // El mock al guardar devuelve el objeto ya modificado (simulando el comportamiento JPA)
        when(repository.save(org.mockito.ArgumentMatchers.any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: ejecutar método a probar.
        SubscriptionResponseDTO result = service.update(subId, requestDTO);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("PRO UPDATE", result.getName());
        assertEquals(129.99, result.getPrice());
        assertEquals(true, result.getActive());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findById(subId);
        verify(repository).save(org.mockito.ArgumentMatchers.any(Subscription.class));

        // Comentario QA: Validar escenario de ID inexistente donde lance RuntimeException "Plan no encontrado."
    }

    @Test
    public void testDelete() {
        // ARRANGE: preparar datos y mocks.
        Long subId = 5L;

        when(repository.existsById(subId)).thenReturn(true);

        // ACT: ejecutar método a probar.
        service.delete(subId);

        // ASSERT: verificar resultado esperado.
        // Al ser void, la validación se hace directamente con el verify.

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).existsById(subId);
        verify(repository).deleteById(subId);

        // Comentario QA: Si el plan no existe, repository.existsById devuelve false y el servicio debe lanzar un RuntimeException "Plan no encontrado."
    }
}
