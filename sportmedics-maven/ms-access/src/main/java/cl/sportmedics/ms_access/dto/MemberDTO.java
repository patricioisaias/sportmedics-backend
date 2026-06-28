package cl.sportmedics.ms_access.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private Long id;
    private String status; // Ejemplo: "Activo", "Inactivo"
}