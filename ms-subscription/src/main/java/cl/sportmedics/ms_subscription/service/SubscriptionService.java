package cl.sportmedics.ms_subscription.service;

import cl.sportmedics.ms_subscription.dto.SubscriptionRequestDTO;
import cl.sportmedics.ms_subscription.dto.SubscriptionResponseDTO;
import java.util.List;

public interface SubscriptionService {
    SubscriptionResponseDTO create(SubscriptionRequestDTO dto);

    List<SubscriptionResponseDTO> getAll();

    SubscriptionResponseDTO getById(Long id);

    SubscriptionResponseDTO update(Long id, SubscriptionRequestDTO dto);

    void delete(Long id);
}