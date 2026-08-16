package cl.sportmedics.ms_member.repository;

import cl.sportmedics.ms_member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByRut(String rut);

    Optional<Member> findByEmail(String email);
}