package cl.sportmedics.ms_employee.service;


import cl.sportmedics.ms_employee.dto.EmployeeRequestDTO;
import cl.sportmedics.ms_employee.dto.EmployeeResponseDTO;
import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO create(EmployeeRequestDTO dto);
    List<EmployeeResponseDTO> getAll();
    EmployeeResponseDTO getById(Long id);
    EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto);
    void delete(Long id);
}