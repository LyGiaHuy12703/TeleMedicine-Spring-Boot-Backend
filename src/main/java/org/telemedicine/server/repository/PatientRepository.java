package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Patients;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patients, String> {
    Boolean existsByEmail(String email);

    Optional<Patients> findByEmail(String email);
}
