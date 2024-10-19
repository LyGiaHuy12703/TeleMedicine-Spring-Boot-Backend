package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Registration;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {
    Registration findByPatientsId(String patientId);
    List<Registration> findByDate(LocalDate date);
}
