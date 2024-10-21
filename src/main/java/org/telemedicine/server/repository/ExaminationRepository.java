package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.Clinic;
import org.telemedicine.server.entity.Examination;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.entity.ServiceEntity;

import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, String> {
    Examination findByClinicId(String clinicId);
    List<Examination> findAllByClinic(Clinic clinic);
    List<Examination> findAllByServiceEntity(ServiceEntity serviceEntity);
    List<Examination> findAllByPatients(Patients patients);

}
