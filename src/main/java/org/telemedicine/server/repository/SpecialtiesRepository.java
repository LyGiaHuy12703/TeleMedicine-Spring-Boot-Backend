package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Specialties;

import java.util.List;

@Repository
public interface SpecialtiesRepository extends JpaRepository<Specialties, String> {
    Boolean existsByName(String name);

}
