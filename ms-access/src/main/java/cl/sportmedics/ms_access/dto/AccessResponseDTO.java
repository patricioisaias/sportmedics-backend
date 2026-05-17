package cl.sportmedics.ms_access.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccessResponseDTO {
    private Long id;
    private Long memberId;
    private LocalDateTime accessDateTime;
    private Boolean granted;
    private String denialReason;
}