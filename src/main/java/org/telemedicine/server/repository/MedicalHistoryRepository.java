package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.MedicalHistory;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, String> {
    List<MedicalHistory> findAllByMedicalRecordBookId(String medicalRecordId);
}
