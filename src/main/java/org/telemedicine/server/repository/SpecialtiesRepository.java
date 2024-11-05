package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Specialties;

import java.util.List;

@Repository
public interface SpecialtiesRepository extends JpaRepository<Specialties, String> {
    Boolean existsByName(String name);
    @Query("SELECT s FROM Specialties s JOIN s.staffs ms WHERE ms IS NOT NULL")
    List<Specialties> findAllWithMedicalStaff();
}
