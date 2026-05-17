package cl.sportmedics.ms_employee.dto;

import lombok.Data;

@Data
public class EmployeeResponseDTO {
    
    private Long id;
    private String rut;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Boolean active = true;

}