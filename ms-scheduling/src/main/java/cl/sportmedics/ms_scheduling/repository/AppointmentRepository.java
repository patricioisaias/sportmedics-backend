package cl.sportmedics.ms_scheduling.repository;

import cl.sportmedics.ms_scheduling.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByMemberId(Long memberId);

    List<Appointment> findByEmployeeIdAndScheduledDateBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
}