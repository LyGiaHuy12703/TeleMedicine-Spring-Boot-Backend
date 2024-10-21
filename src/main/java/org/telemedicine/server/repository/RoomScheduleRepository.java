package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
                                                 @Param("clinic") String clinic,
                                                 @Param("medicalStaff") String medicalStaff);
}
