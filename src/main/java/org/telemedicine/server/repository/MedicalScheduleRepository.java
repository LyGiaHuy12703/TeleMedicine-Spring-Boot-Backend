package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.MedicalSchedule;
import org.telemedicine.server.enums.StatusSchedule;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalScheduleRepository extends JpaRepository<MedicalSchedule, String> {
    List<MedicalSchedule> findByAppointmentDate(LocalDate date);
    List<MedicalSchedule> findByPatientsId(String id);
    long countByStatus(StatusSchedule status);
}
