package cl.sportmedics.ms_access.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.sportmedics.ms_access.dto.MemberDTO;

@FeignClient(name = "ms-member")
public interface MemberFeignClient {

    // Ajusta la ruta a la que realmente expone ms-member (ej: /api/members/{id})
    @GetMapping("/api/members/{id}")
    MemberDTO getMemberById(@PathVariable("id") Long id);
}