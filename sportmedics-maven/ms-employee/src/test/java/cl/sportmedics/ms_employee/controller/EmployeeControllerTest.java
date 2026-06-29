package cl.sportmedics.ms_employee.controller;

import cl.sportmedics.ms_employee.dto.EmployeeRequestDTO;
import cl.sportmedics.ms_employee.dto.EmployeeResponseDTO;
import cl.sportmedics.ms_employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @Test
    public void testCreate() throws Exception {
        // ARRANGE: preparar datos y mocks.
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setFirstName("Juan");
        requestDTO.setLastName("Perez");
        requestDTO.setEmail("juan@sportmedics.cl");
        requestDTO.setRole("ADMIN");

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setRut("12345678-9");
        responseDTO.setFirstName("Juan");
        responseDTO.setLastName("Perez");
        responseDTO.setEmail("juan@sportmedics.cl");
        responseDTO.setRole("ADMIN");

        when(service.create(org.mockito.ArgumentMatchers.any(EmployeeRequestDTO.class))).thenReturn(responseDTO);

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rut\":\"12345678-9\", \"firstName\": \"Juan\", \"lastName\": \"Perez\", \"email\":\"juan@sportmedics.cl\", \"role\":\"ADMIN\"}"))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Perez"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(service).create(org.mockito.ArgumentMatchers.any(EmployeeRequestDTO.class));

        // Comentario QA: Si se ingresa un campo vacío que sea obligatorio en el DTO, se debe verificar que el endpoint responda con HTTP 400 Bad Request.
    }

    @Test
    public void testGetAll() throws Exception {
        // ARRANGE: preparar datos y mocks.
        EmployeeResponseDTO e1 = new EmployeeResponseDTO();
        e1.setId(1L);
        e1.setFirstName("Pedro");
        
        EmployeeResponseDTO e2 = new EmployeeResponseDTO();
        e2.setId(2L);
        e2.setFirstName("Maria");
        
        java.util.List<EmployeeResponseDTO> mockList = java.util.Arrays.asList(e1, e2);
        
        when(service.getAll()).thenReturn(mockList);

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Pedro"))
                .andExpect(jsonPath("$[1].firstName").value("Maria"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(service).getAll();

        // Comentario QA: Si se esperaban registros pero el servicio retornó un arreglo vacío [], el test podría fallar si se asume tamaño fijo de 2. QA debe validar si el servicio realmente consultó a la BD.
    }

    @Test
    public void testGetById() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long employeeId = 5L;
        EmployeeResponseDTO e1 = new EmployeeResponseDTO();
        e1.setId(employeeId);
        e1.setFirstName("Laura");
        
        when(service.getById(employeeId)).thenReturn(e1);

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.firstName").value("Laura"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(service).getById(employeeId);

        // Comentario QA: Si el registro no existe, el test podría configurarse para lanzar un 404 Not Found. Esto debe estar cubierto por otra prueba o documentado si hay un GlobalExceptionHandler que capture la excepción.
    }

    @Test
    public void testUpdate() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long employeeId = 5L;
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setFirstName("Laura Modificada");
        requestDTO.setLastName("Gonzalez");
        requestDTO.setEmail("laura@sportmedics.cl");
        requestDTO.setRole("ADMIN");

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(employeeId);
        responseDTO.setRut("12345678-9");
        responseDTO.setFirstName("Laura Modificada");
        responseDTO.setLastName("Gonzalez");
        responseDTO.setEmail("laura@sportmedics.cl");
        responseDTO.setRole("ADMIN");

        when(service.update(org.mockito.ArgumentMatchers.eq(employeeId), org.mockito.ArgumentMatchers.any(EmployeeRequestDTO.class))).thenReturn(responseDTO);

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rut\":\"12345678-9\", \"firstName\": \"Laura Modificada\", \"lastName\": \"Gonzalez\", \"email\":\"laura@sportmedics.cl\", \"role\":\"ADMIN\"}"))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.firstName").value("Laura Modificada"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(service).update(org.mockito.ArgumentMatchers.eq(employeeId), org.mockito.ArgumentMatchers.any(EmployeeRequestDTO.class));

        // Comentario QA: Si el endpoint retorna 400, verificar que el JSON enviado cumpla con las restricciones de @Valid en EmployeeRequestDTO.
    }

    @Test
    public void testDelete() throws Exception {
        // ARRANGE: preparar datos y mocks.
        Long employeeId = 5L;

        // ACT: ejecutar método o endpoint.
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/employees/{id}", employeeId))

        // ASSERT: verificar resultado esperado.
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock si corresponde.
        verify(service).delete(employeeId);

        // Comentario QA: Si el endpoint retorna 200 en lugar de 204, se debe pedir a desarrollo que use ResponseEntity.noContent() para deletes exitosos, según las buenas prácticas REST.
    }
}
