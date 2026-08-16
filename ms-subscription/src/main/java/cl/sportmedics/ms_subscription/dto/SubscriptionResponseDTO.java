package cl.sportmedics.ms_subscription.dto;

import lombok.Data;

@Data
public class SubscriptionResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer durationMonths;
    private Boolean active;
}