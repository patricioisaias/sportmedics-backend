package cl.sportmedics.ms_employee.service;

import cl.sportmedics.ms_employee.dto.EmployeeRequestDTO;
import cl.sportmedics.ms_employee.dto.EmployeeResponseDTO;
import cl.sportmedics.ms_employee.entity.Employee;
import cl.sportmedics.ms_employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        log.info("Registrando nuevo empleado con RUT: {}", dto.getRut());
        
        if (repository.findByRut(dto.getRut()).isPresent()) {
            throw new RuntimeException("El RUT del empleado ya está registrado.");
        }
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico del empleado ya está en uso.");
        }

        Employee employee = Employee.builder()
                .rut(dto.getRut())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .role(dto.getRole().toUpperCase()) // Normalizamos a mayúsculas
                .active(dto.getActive())
                .build();

        Employee saved = repository.save(employee);
        log.info("Empleado registrado exitosamente con ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    public List<EmployeeResponseDTO> getAll() {
        log.info("Consultando la lista completa del staff.");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        log.info("Buscando empleado con ID: {}", id);
        return repository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado."));
    }

    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        log.info("Actualizando datos del empleado ID: {}", id);
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado."));

        employee.setRut(dto.getRut());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setRole(dto.getRole().toUpperCase());
        employee.setActive(dto.getActive());

        log.info("Datos del empleado ID: {} actualizados.", id);
        return mapToDTO(repository.save(employee));
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando empleado ID: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado.");
        }
        repository.deleteById(id);
        log.info("Empleado ID: {} eliminado.", id);
    }

    private EmployeeResponseDTO mapToDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setRut(employee.getRut());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setRole(employee.getRole());
        dto.setActive(employee.getActive());
        return dto;
    }
}