package cl.sportmedics.ms_member.service;

import cl.sportmedics.ms_member.dto.MemberRequestDTO;
import cl.sportmedics.ms_member.dto.MemberResponseDTO;
import cl.sportmedics.ms_member.entity.Member;
import cl.sportmedics.ms_member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository repository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void testCreate() {
        MemberRequestDTO request = new MemberRequestDTO();
        request.setRut("12345678-9");
        request.setFirstName("Juan");
        request.setLastName("Perez");
        request.setEmail("juan@example.com");
        request.setPhone("+56912345678");
        request.setActive(true);

        Member savedMember = Member.builder()
                .id(1L)
                .rut("12345678-9")
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@example.com")
                .phone("+56912345678")
                .active(true)
                .build();

        Mockito.when(repository.findByRut(request.getRut())).thenReturn(Optional.empty());
        Mockito.when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Mockito.when(repository.save(any(Member.class))).thenReturn(savedMember);

        MemberResponseDTO response = memberService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("12345678-9", response.getRut());

        Mockito.verify(repository, Mockito.times(1)).findByRut(request.getRut());
        Mockito.verify(repository, Mockito.times(1)).findByEmail(request.getEmail());
        Mockito.verify(repository, Mockito.times(1)).save(any(Member.class));
    }

    @Test
    void testGetAll() {
        Member member1 = Member.builder().id(1L).firstName("Juan").build();
        Member member2 = Member.builder().id(2L).firstName("Maria").build();

        Mockito.when(repository.findAll()).thenReturn(List.of(member1, member2));

        List<MemberResponseDTO> responses = memberService.getAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Juan", responses.get(0).getFirstName());

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Member member = Member.builder().id(id).firstName("Juan").build();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(member));

        MemberResponseDTO response = memberService.getById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Juan", response.getFirstName());

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        MemberRequestDTO request = new MemberRequestDTO();
        request.setRut("12345678-9");
        request.setFirstName("Juan Modificado");
        request.setLastName("Perez");
        request.setEmail("juan@example.com");
        request.setActive(true);

        Member existingMember = Member.builder().id(id).firstName("Juan").build();
        Member updatedMember = Member.builder().id(id).firstName("Juan Modificado").build();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingMember));
        Mockito.when(repository.save(any(Member.class))).thenReturn(updatedMember);

        MemberResponseDTO response = memberService.update(id, request);

        assertNotNull(response);
        assertEquals("Juan Modificado", response.getFirstName());

        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Mockito.verify(repository, Mockito.times(1)).save(any(Member.class));
    }

    @Test
    void testDelete() {
        Long id = 1L;
        Mockito.when(repository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(id);

        memberService.delete(id);

        Mockito.verify(repository, Mockito.times(1)).existsById(id);
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }
}
