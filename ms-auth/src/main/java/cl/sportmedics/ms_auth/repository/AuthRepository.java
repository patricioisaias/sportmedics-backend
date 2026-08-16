package cl.sportmedics.ms_auth.repository;

import cl.sportmedics.ms_auth.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByUsername(String username);
}