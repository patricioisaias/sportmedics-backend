package cl.sportmedics.ms_member.controller;

import cl.sportmedics.ms_member.dto.MemberRequestDTO;
import cl.sportmedics.ms_member.dto.MemberResponseDTO;
import cl.sportmedics.ms_member.service.MemberService;
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

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        // ARRANGE: preparar datos y mocks.
        MemberRequestDTO request = new MemberRequestDTO();
        request.setRut("12345678-9");
        request.setFirstName("Juan");
        request.setLastName("Perez");
        request.setEmail("juan@example.com");
        request.setPhone("+56912345678");
        request.setActive(true);

        MemberResponseDTO response = new MemberResponseDTO();
        response.setId(1L);
        response.setRut("12345678-9");
        response.setFirstName("Juan");
        response.setLastName("Perez");
        response.setEmail("juan@example.com");

        Mockito.when(memberService.create(any(MemberRequestDTO.class))).thenReturn(response);

        // ACT: ejecutar método o endpoint.
        // ASSERT: verificar resultado esperado.
        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));

        // VERIFY: comprobar llamadas al mock si corresponde.
        Mockito.verify(memberService, Mockito.times(1)).create(any(MemberRequestDTO.class));
        
        // Comentario final con un caso hipotético de falla para QA
        // Se esperaba: HTTP 201 Created
        // Se obtuvo: HTTP 400 Bad Request
        // Explicación: QA intenta crear un miembro con un RUT inválido ("123456") y el servidor lo rechaza. Esto es correcto gracias al @Pattern en el DTO, pero si envía datos buenos y aún da 400, Desarrollo debe revisar las validaciones del RequestBody.
    }

    @Test
    void testGetAll() throws Exception {
        MemberResponseDTO response1 = new MemberResponseDTO();
        response1.setId(1L);
        response1.setFirstName("Juan");

        MemberResponseDTO response2 = new MemberResponseDTO();
        response2.setId(2L);
        response2.setFirstName("Maria");

        Mockito.when(memberService.getAll()).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));

        Mockito.verify(memberService, Mockito.times(1)).getAll();
    }

    @Test
    void testGetById() throws Exception {
        Long id = 1L;
        MemberResponseDTO response = new MemberResponseDTO();
        response.setId(id);
        response.setFirstName("Juan");

        Mockito.when(memberService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/members/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("Juan"));

        Mockito.verify(memberService, Mockito.times(1)).getById(id);
    }

    @Test
    void testUpdate() throws Exception {
        Long id = 1L;
        MemberRequestDTO request = new MemberRequestDTO();
        request.setRut("12345678-9");
        request.setFirstName("Juan Modificado");
        request.setLastName("Perez");
        request.setEmail("juan_mod@example.com");
        request.setPhone("+56912345678");
        request.setActive(true);

        MemberResponseDTO response = new MemberResponseDTO();
        response.setId(id);
        response.setFirstName("Juan Modificado");

        Mockito.when(memberService.update(org.mockito.ArgumentMatchers.eq(id), any(MemberRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/members/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan Modificado"));

        Mockito.verify(memberService, Mockito.times(1)).update(org.mockito.ArgumentMatchers.eq(id), any(MemberRequestDTO.class));
    }

    @Test
    void testDelete() throws Exception {
        Long id = 1L;
        Mockito.doNothing().when(memberService).delete(id);

        mockMvc.perform(delete("/api/members/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(memberService, Mockito.times(1)).delete(id);
    }
}
