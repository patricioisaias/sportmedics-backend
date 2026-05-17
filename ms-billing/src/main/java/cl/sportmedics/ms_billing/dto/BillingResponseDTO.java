package cl.sportmedics.ms_billing.dto;

import lombok.Data;

@Data
public class BillingResponseDTO {
    private Long id;
    private String rut;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean active;
}