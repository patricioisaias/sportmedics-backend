package cl.sportmedics.ms_access.service;

import cl.sportmedics.ms_access.client.BillingFeignClient;
import cl.sportmedics.ms_access.client.MemberFeignClient;
import cl.sportmedics.ms_access.dto.AccessRequestDTO;
import cl.sportmedics.ms_access.dto.AccessResponseDTO;
import cl.sportmedics.ms_access.entity.Access;
import cl.sportmedics.ms_access.repository.AccessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessServiceTest {

    @Mock
    private AccessRepository repository;

    @Mock
    private MemberFeignClient memberClient;

    @Mock
    private BillingFeignClient billingClient;

    @InjectMocks
    private AccessServiceImpl accessService;

    @Test
    public void testGetAllAccessLogs() {
        // ARRANGE: preparar datos y mocks.
        Access access1 = Access.builder()
                .id(1L)
                .memberId(10L)
                .granted(true)
                .accessDateTime(LocalDateTime.now())
                .build();

        Access access2 = Access.builder()
                .id(2L)
                .memberId(20L)
                .granted(false)
                .denialReason("Socio Inactivo")
                .accessDateTime(LocalDateTime.now())
                .build();

        List<Access> mockList = Arrays.asList(access1, access2);
        when(repository.findAll()).thenReturn(mockList);

        // ACT: ejecutar método o endpoint.
        List<AccessResponseDTO> result = accessService.getAllAccessLogs();

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(2, result.size());
        
        assertEquals(1L, result.get(0).getId());
        assertEquals(10L, result.get(0).getMemberId());
        assertEquals(true, result.get(0).getGranted());
        
        assertEquals(2L, result.get(1).getId());
        assertEquals(20L, result.get(1).getMemberId());
        assertEquals(false, result.get(1).getGranted());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findAll();
        
        // Comentario QA: Si se esperaban 2 resultados y se obtuvo 0, QA debería reportarlo indicando falla en la lógica de listado. Desarrollo deberá revisar si el método de mapeo de Entity a DTO está retornando nulos.
    }

    @Test
    public void testGetLogsByMemberId() {
        // ARRANGE: preparar datos y mocks.
        Long memberId = 88L;
        
        Access access = Access.builder()
                .id(5L)
                .memberId(memberId)
                .granted(true)
                .accessDateTime(LocalDateTime.now())
                .build();

        List<Access> mockList = Arrays.asList(access);
        when(repository.findByMemberId(memberId)).thenReturn(mockList);

        // ACT: ejecutar método o endpoint.
        List<AccessResponseDTO> result = accessService.getLogsByMemberId(memberId);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId());
        assertEquals(memberId, result.get(0).getMemberId());
        assertEquals(true, result.get(0).getGranted());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findByMemberId(memberId);
        
        // Comentario QA: Si el test arroja un NullPointerException durante la aserción de result.get(0), QA debe indicar que el servicio devolvió null en vez de una lista vacía. Desarrollo deberá revisar la implementación de repository.findByMemberId().
    }

    @Test
    public void testRegisterAccessAttempt_Success() {
        // ARRANGE: preparar datos y mocks.
        AccessRequestDTO requestDTO = new AccessRequestDTO();
        requestDTO.setMemberId(33L);

        // Mockeamos respuesta positiva de Feign ms-member
        cl.sportmedics.ms_access.dto.MemberDTO mockMember = new cl.sportmedics.ms_access.dto.MemberDTO();
        mockMember.setStatus("Activo");
        when(memberClient.getMemberById(33L)).thenReturn(mockMember);

        // Mockeamos respuesta positiva de Feign ms-billing (sin deuda)
        cl.sportmedics.ms_access.dto.BillingStatusDTO mockBilling = new cl.sportmedics.ms_access.dto.BillingStatusDTO();
        mockBilling.setHasDebt(false);
        when(billingClient.getBillingStatus(33L)).thenReturn(mockBilling);

        // Mockeamos el guardado en base de datos
        Access savedAccess = Access.builder()
                .id(999L)
                .memberId(33L)
                .granted(true)
                .accessDateTime(LocalDateTime.now())
                .build();
        when(repository.save(org.mockito.ArgumentMatchers.any(Access.class))).thenReturn(savedAccess);

        // ACT: ejecutar método o endpoint.
        AccessResponseDTO result = accessService.registerAccessAttempt(requestDTO);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(999L, result.getId());
        assertEquals(33L, result.getMemberId());
        assertEquals(true, result.getGranted());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(memberClient).getMemberById(33L);
        verify(billingClient).getBillingStatus(33L);
        verify(repository).save(org.mockito.ArgumentMatchers.any(Access.class));
        
        // Comentario QA: Si el test fallara indicando un "NullPointerException" al revisar 'member.getStatus()', significa que Feign está fallando en la des-serialización o que el DTO viene vacío, QA deberá levantar un bug crítico de comunicación entre microservicios.
    }
}
