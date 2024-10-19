package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Medicine;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByDrugId(String id);
    Boolean existsByName(String medicineName);
}
