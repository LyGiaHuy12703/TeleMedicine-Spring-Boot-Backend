package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Clinic;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Boolean existsByName(String name);

}
