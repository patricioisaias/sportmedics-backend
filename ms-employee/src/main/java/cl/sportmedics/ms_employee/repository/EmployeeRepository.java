package cl.sportmedics.ms_employee.repository;

import cl.sportmedics.ms_employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByRut(String rut);
    Optional<Employee> findByEmail(String email);
}
