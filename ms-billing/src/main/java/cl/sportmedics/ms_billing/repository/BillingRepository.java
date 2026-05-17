package cl.sportmedics.ms_billing.repository;


import cl.sportmedics.ms_billing.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByMemberId(Long memberId);
}