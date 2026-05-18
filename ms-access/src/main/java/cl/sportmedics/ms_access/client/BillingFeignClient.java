package cl.sportmedics.ms_access.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.sportmedics.ms_access.dto.BillingStatusDTO;

@FeignClient(name = "ms-billing")
public interface BillingFeignClient {

    // Ajusta la ruta a la que realmente expone ms-billing
    @GetMapping("/api/billing/status/{memberId}")
    BillingStatusDTO getBillingStatus(@PathVariable("memberId") Long memberId);
}