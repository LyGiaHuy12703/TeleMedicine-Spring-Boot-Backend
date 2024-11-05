package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Clinic;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.RoomSchedule;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, String> {
    RoomSchedule findByClinicId(String id);
    List<RoomSchedule> findByDate(LocalDate date);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM RoomSchedule a " +
            "WHERE a.date = :date AND a.clinic = :clinic AND a.medicalStaff = :medicalStaff")
    Boolean existsByDateAndClinicAndMedicalStaff(@Param("date") LocalDate date,
                                                 @Param("clinic") Clinic clinic,
                                                 @Param("medicalStaff") MedicalStaff medicalStaff);
    @Query("SELECT COUNT(rs) FROM RoomSchedule rs WHERE rs.date = :date AND rs.clinic = :clinic")
    int countByDateAndClinic(@Param("date") LocalDate date, @Param("clinic") Clinic clinic);
    @Query("SELECT COUNT(rs) > 0 FROM RoomSchedule rs WHERE rs.date = :date AND rs.medicalStaff = :staff")
    boolean existsByDateAndMedicalStaff(@Param("date") LocalDate date, @Param("staff") MedicalStaff staff);

}
