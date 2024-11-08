package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.MedicalSchedule;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.enums.StatusSchedule;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MedicalScheduleRepository extends JpaRepository<MedicalSchedule, String> {
    List<MedicalSchedule> findByAppointmentDateAndPatients(LocalDate date, Patients patient);
    List<MedicalSchedule> findByAppointmentDate(LocalDate date);
    List<MedicalSchedule> findByPatientsId(String id);
    List<MedicalSchedule> findByStatus(StatusSchedule statusSchedule);
    long countByStatus(StatusSchedule status);
    boolean existsByPatientsAndAppointmentDateAndAppointmentTime(Patients patients, LocalDate appointmentDate, Time appointmentTime);
}
