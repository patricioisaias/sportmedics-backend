package cl.sportmedics.ms_billing.service;


import cl.sportmedics.ms_billing.dto.BillingRequestDTO;
import cl.sportmedics.ms_billing.dto.BillingResponseDTO;
import java.util.List;

public interface BillingService {
    BillingResponseDTO create(BillingRequestDTO dto);
    List<BillingResponseDTO> getAll();
    BillingResponseDTO getById(Long id);
    BillingResponseDTO update(Long id, BillingRequestDTO dto);
    void delete(Long id);
    void processPayment(Long invoiceId, Double amount, String method, String ref);
}