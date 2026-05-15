package cl.sportmedics.ms_subscription.service;

import cl.sportmedics.ms_subscription.dto.SubscriptionRequestDTO;
import cl.sportmedics.ms_subscription.dto.SubscriptionResponseDTO;
import cl.sportmedics.ms_subscription.entity.Subscription;
import cl.sportmedics.ms_subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;

    @Override
    public SubscriptionResponseDTO create(SubscriptionRequestDTO dto) {
        log.info("Iniciando creación de plan: {}", dto.getName());
        if (repository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("El plan ya existe en el sistema.");
        }
        Subscription subscription = Subscription.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .durationMonths(dto.getDurationMonths())
                .active(dto.getActive())
                .build();
        Subscription saved = repository.save(subscription);
        log.info("Plan creado exitosamente con ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    public List<SubscriptionResponseDTO> getAll() {
        log.info("Consultando todos los planes");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponseDTO getById(Long id) {
        log.info("Consultando plan ID: {}", id);
        return repository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado."));
    }

    @Override
    public SubscriptionResponseDTO update(Long id, SubscriptionRequestDTO dto) {
        log.info("Actualizando plan ID: {}", id);
        Subscription sub = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado."));

        sub.setName(dto.getName());
        sub.setPrice(dto.getPrice());
        sub.setDurationMonths(dto.getDurationMonths());
        sub.setActive(dto.getActive());

        log.info("Plan ID: {} actualizado", id);
        return mapToDTO(repository.save(sub));
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando plan ID: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Plan no encontrado.");
        }
        repository.deleteById(id);
        log.info("Plan ID: {} eliminado exitosamente", id);
    }

    private SubscriptionResponseDTO mapToDTO(Subscription sub) {
        SubscriptionResponseDTO dto = new SubscriptionResponseDTO();
        dto.setId(sub.getId());
        dto.setName(sub.getName());
        dto.setPrice(sub.getPrice());
        dto.setDurationMonths(sub.getDurationMonths());
        dto.setActive(sub.getActive());
        return dto;
    }
}