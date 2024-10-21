package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.MedicalRecordBook;

@Repository
public interface MedicalRecordBookRepository extends JpaRepository<MedicalRecordBook, String> {
    MedicalRecordBook findByPatientsId(String patientID);
}
