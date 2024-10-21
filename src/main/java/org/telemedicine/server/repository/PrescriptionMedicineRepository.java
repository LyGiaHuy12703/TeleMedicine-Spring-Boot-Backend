package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Medicine;
import org.telemedicine.server.entity.PrescriptionMedicine;

import java.util.List;

@Repository
public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicine, String> {
    List<PrescriptionMedicine> findByMedicine(Medicine medicine);
}
