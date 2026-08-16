package cl.sportmedics.ms_employee.service;

import cl.sportmedics.ms_employee.dto.EmployeeResponseDTO;
import cl.sportmedics.ms_employee.entity.Employee;
import cl.sportmedics.ms_employee.repository.EmployeeRepository;
import cl.sportmedics.ms_employee.service.EmployeeServiceImpl;

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
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    public void testGetAll() {
        // ARRANGE: preparar datos y mocks.
        Employee emp1 = Employee.builder()
                .id(1L)
                .firstName("Carlos")
                .role("ADMIN")
                .build();
        
        Employee emp2 = Employee.builder()
                .id(2L)
                .firstName("Ana")
                .role("TEACHER")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(emp1, emp2));

        // ACT: ejecutar método a probar.
        List<EmployeeResponseDTO> result = service.getAll();

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Carlos", result.get(0).getFirstName());
        assertEquals("Ana", result.get(1).getFirstName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findAll();

        // Comentario QA: Validar que el mapeo preserve el rol y que ningún dato se trunque al pasar de Entity a DTO.
    }

    @Test
    public void testGetById() {
        // ARRANGE: preparar datos y mocks.
        Long employeeId = 5L;
        Employee emp = Employee.builder()
                .id(employeeId)
                .firstName("Luis")
                .role("ADMIN")
                .build();

        when(repository.findById(employeeId)).thenReturn(java.util.Optional.of(emp));

        // ACT: ejecutar método a probar.
        EmployeeResponseDTO result = service.getById(employeeId);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Luis", result.getFirstName());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findById(employeeId);

        // Comentario QA: Si se pasa un ID que no existe, el servicio lanzará un RuntimeException. QA podría pedir que se especifique un tipo de excepción más personalizado, como EntityNotFoundException.
    }

    @Test
    public void testCreate_Success() {
        // ARRANGE: preparar datos y mocks.
        cl.sportmedics.ms_employee.dto.EmployeeRequestDTO requestDTO = new cl.sportmedics.ms_employee.dto.EmployeeRequestDTO();
        requestDTO.setRut("11111111-1");
        requestDTO.setFirstName("Valeria");
        requestDTO.setEmail("vale@sportmedics.cl");
        requestDTO.setRole("admin");
        requestDTO.setActive(true);

        when(repository.findByRut("11111111-1")).thenReturn(java.util.Optional.empty());
        when(repository.findByEmail("vale@sportmedics.cl")).thenReturn(java.util.Optional.empty());

        Employee savedEmployee = Employee.builder()
                .id(10L)
                .rut("11111111-1")
                .firstName("Valeria")
                .email("vale@sportmedics.cl")
                .role("ADMIN")
                .active(true)
                .build();

        when(repository.save(org.mockito.ArgumentMatchers.any(Employee.class))).thenReturn(savedEmployee);

        // ACT: ejecutar método a probar.
        EmployeeResponseDTO result = service.create(requestDTO);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Valeria", result.getFirstName());
        assertEquals("ADMIN", result.getRole()); // Validando normalización a mayúsculas

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findByRut("11111111-1");
        verify(repository).findByEmail("vale@sportmedics.cl");
        verify(repository).save(org.mockito.ArgumentMatchers.any(Employee.class));

        // Comentario QA: Validar escenarios de fallo donde el RUT o Email ya existen, el servicio debe lanzar una excepción para evitar duplicados.
    }

    @Test
    public void testUpdate() {
        // ARRANGE: preparar datos y mocks.
        Long employeeId = 5L;
        cl.sportmedics.ms_employee.dto.EmployeeRequestDTO requestDTO = new cl.sportmedics.ms_employee.dto.EmployeeRequestDTO();
        requestDTO.setRut("22222222-2");
        requestDTO.setFirstName("Valeria Editada");
        requestDTO.setLastName("Gomez");
        requestDTO.setEmail("vale2@sportmedics.cl");
        requestDTO.setRole("admin");
        requestDTO.setActive(true);

        Employee existingEmployee = Employee.builder()
                .id(employeeId)
                .rut("11111111-1")
                .firstName("Valeria")
                .lastName("Lopez")
                .email("vale@sportmedics.cl")
                .role("USER")
                .active(false)
                .build();

        // El servicio primero busca
        when(repository.findById(employeeId)).thenReturn(java.util.Optional.of(existingEmployee));

        // El mock al guardar devuelve el objeto ya modificado (por referencia o simulando el retorno)
        when(repository.save(org.mockito.ArgumentMatchers.any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: ejecutar método a probar.
        EmployeeResponseDTO result = service.update(employeeId, requestDTO);

        // ASSERT: verificar resultado esperado.
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Valeria Editada", result.getFirstName());
        assertEquals("ADMIN", result.getRole()); // Valida cambio a mayúscula
        assertEquals(true, result.getActive());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).findById(employeeId);
        verify(repository).save(org.mockito.ArgumentMatchers.any(Employee.class));

        // Comentario QA: Validar escenario de ID inexistente donde lance RuntimeException "Empleado no encontrado."
    }

    @Test
    public void testDelete() {
        // ARRANGE: preparar datos y mocks.
        Long employeeId = 5L;

        when(repository.existsById(employeeId)).thenReturn(true);

        // ACT: ejecutar método a probar.
        service.delete(employeeId);

        // ASSERT: verificar resultado esperado.
        // Al ser un método void, la afirmación principal recae en el VERIFY de las llamadas.

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(repository).existsById(employeeId);
        verify(repository).deleteById(employeeId);

        // Comentario QA: Si el registro no existe, repository.existsById devuelve false y el servicio debe lanzar un RuntimeException "Empleado no encontrado."
    }
}
