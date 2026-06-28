package cl.sportmedics.ms_access.dto;

import lombok.Data;

@Data
public class BillingStatusDTO {
    private Long memberId;
    private boolean hasDebt; // true si debe, false si está al día
    // Alternativamente puedes usar un String status ("PAGADO", "PENDIENTE")
}