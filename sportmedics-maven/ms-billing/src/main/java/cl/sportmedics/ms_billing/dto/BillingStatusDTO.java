package cl.sportmedics.ms_billing.dto;

import lombok.Data;

@Data
public class BillingStatusDTO {
    private Long memberId;
    private boolean hasDebt;
}