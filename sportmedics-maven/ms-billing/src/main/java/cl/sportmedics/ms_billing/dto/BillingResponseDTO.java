package cl.sportmedics.ms_billing.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BillingResponseDTO {
    
    private Long id;
    private Long memberId;
    private Long subscriptionId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private Double totalAmount;
    private String status;

}