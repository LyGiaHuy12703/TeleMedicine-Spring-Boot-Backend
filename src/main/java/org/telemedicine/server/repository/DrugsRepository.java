package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Drug;

import java.util.Optional;

@Repository
public interface DrugsRepository extends JpaRepository<Drug, String> {
    Boolean existsByName(String name);

}
