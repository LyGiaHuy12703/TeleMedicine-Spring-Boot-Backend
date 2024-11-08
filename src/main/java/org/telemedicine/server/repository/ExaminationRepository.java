package org.telemedicine.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telemedicine.server.entity.*;
import org.telemedicine.server.enums.StatusSchedule;

import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, String> {
    Examination findByClinicId(String clinicId);
    List<Examination> findAllByClinic(Clinic clinic);
    List<Examination> findAllByServiceEntity(ServiceEntity serviceEntity);
    List<Examination> findAllByPatients(Patients patients);
    Examination findByMedicalSchedule(MedicalSchedule medicalSchedule);
    List<Examination> findAllByClinicAndMedicalScheduleStatus(Clinic clinic, StatusSchedule status);


}
