package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalStaffRepository extends JpaRepository<MedicalStaff, String> {
    Optional<MedicalStaff> findByEmail(String email);
    Boolean existsByEmail(String email);
    MedicalStaff findMedicalStaffById(String id);
    List<MedicalStaff> findMedicalStaffBySpecialtiesId(String id);
}
