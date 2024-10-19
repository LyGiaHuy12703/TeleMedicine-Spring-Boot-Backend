package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telemedicine.server.entity.Medicine;
import org.telemedicine.server.entity.PrescriptionMedicine;

import java.util.List;

public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicineRepository, Long> {
    List<PrescriptionMedicine> findByMedicine(Medicine medicine);
}
