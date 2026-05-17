package cl.sportmedics.ms_access.repository;


import cl.sportmedics.ms_access.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    // Permite buscar el historial de accesos de un alumno específico
    List<Access> findByMemberId(Long memberId);
}