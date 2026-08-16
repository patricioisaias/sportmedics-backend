package cl.sportmedics.ms_billing.service;

import cl.sportmedics.ms_billing.dto.BillingRequestDTO;
import cl.sportmedics.ms_billing.dto.BillingResponseDTO;
import cl.sportmedics.ms_billing.entity.Billing;
import cl.sportmedics.ms_billing.entity.Payment;
import cl.sportmedics.ms_billing.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRepository repository;

    @Override
    @Transactional
    public BillingResponseDTO create(BillingRequestDTO dto) {
        log.info("Generando nuevo cobro para el miembro ID: {}", dto.getMemberId());

        Billing billing = Billing.builder()
                .memberId(dto.getMemberId())
                .subscriptionId(dto.getSubscriptionId())
                .issueDate(dto.getIssueDate())
                .dueDate(dto.getDueDate())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus().toUpperCase())
                .build();

        Billing saved = repository.save(billing);
        return mapToDTO(saved);
    }

    @Override
    public List<BillingResponseDTO> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public BillingResponseDTO getById(Long id) {
        return repository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Cobro no encontrado."));
    }

    @Override
    @Transactional
    public BillingResponseDTO update(Long id, BillingRequestDTO dto) {
        Billing billing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cobro no encontrado."));

        billing.setTotalAmount(dto.getTotalAmount());
        billing.setStatus(dto.getStatus().toUpperCase());
        billing.setDueDate(dto.getDueDate());

        return mapToDTO(repository.save(billing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cobro no encontrado.");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void processPayment(Long billingId, Double amount, String method, String ref) {
        log.info("Procesando pago de ${} para el cobro ID: {}", amount, billingId);
        Billing billing = repository.findById(billingId)
                .orElseThrow(() -> new RuntimeException("Cobro no encontrado."));

        if ("PAID".equals(billing.getStatus())) {
            throw new RuntimeException("Este cobro ya se encuentra pagado.");
        }

        Payment payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .amountPaid(amount)
                .paymentMethod(method.toUpperCase())
                .transactionRef(ref)
                .billing(billing) // Asociamos al cobro
                .build();

        billing.getPayments().add(payment);

        Double totalPaid = billing.getPayments().stream().mapToDouble(Payment::getAmountPaid).sum();
        if (totalPaid >= billing.getTotalAmount()) {
            billing.setStatus("PAID");
        }

        repository.save(billing);
    }

    private BillingResponseDTO mapToDTO(Billing billing) {
        BillingResponseDTO dto = new BillingResponseDTO();
        dto.setId(billing.getId());
        dto.setMemberId(billing.getMemberId());
        dto.setSubscriptionId(billing.getSubscriptionId());
        dto.setIssueDate(billing.getIssueDate());
        dto.setDueDate(billing.getDueDate());
        dto.setTotalAmount(billing.getTotalAmount());
        dto.setStatus(billing.getStatus());
        return dto;
    }

    @Override
    public boolean checkDebtStatus(Long memberId) {
        log.info("Verificando deudas para el socio ID: {}", memberId);
        List<Billing> bills = repository.findByMemberId(memberId);

        // Si el socio no tiene boletas, no tiene deudas (false)
        if (bills.isEmpty()) {
            return false;
        }

        // Si hay AL MENOS UNA boleta con estado "PENDING", entonces tiene deuda (true)
        return bills.stream()
                .anyMatch(bill -> "PENDING".equalsIgnoreCase(bill.getStatus()));
    }

    @Override
    @Transactional
    public void initializeAccount(Long memberId) {
        log.info("Inicializando estado financiero (Defensa en profundidad) para socio ID: {}", memberId);
        List<Billing> bills = repository.findByMemberId(memberId);

        // Siguiendo la lógica de la rúbrica (como el stock en 0):
        // Si no existe historial, creamos una "boleta de matrícula/inscripción" de
        // costo $0 pagada,
        // o simplemente lo dejamos registrado en los logs para futuros cobros.
        if (bills.isEmpty()) {
            Billing welcomeBilling = Billing.builder()
                    .memberId(memberId)
                    .subscriptionId(0L) // 0 o ID de membresía por defecto
                    .issueDate(java.time.LocalDate.now())
                    .dueDate(java.time.LocalDate.now())
                    .totalAmount(0.0)
                    .status("PAID")
                    .build();
            repository.save(welcomeBilling);
            log.info("Cuenta inicializada con éxito para el socio {}.", memberId);
        }
    }
}