//package org.telemedicine.server.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Service;
//import org.telemedicine.server.dto.prescriptionMedicine.PrescriptionMedicineRequest;
//import org.telemedicine.server.dto.prescriptionMedicine.PrescriptionMedicineResponse;
//import org.telemedicine.server.entity.Medicine;
//import org.telemedicine.server.entity.Prescription;
//import org.telemedicine.server.entity.PrescriptionMedicine;
//import org.telemedicine.server.exception.AppException;
//import org.telemedicine.server.mapper.PrescriptionMedicineMapper;
//import org.telemedicine.server.repository.MedicineRepository;
//import org.telemedicine.server.repository.PrescriptionMedicineRepository;
//import org.telemedicine.server.repository.PrescriptionRepository;
//
//@Service
//public class PrescriptionMedicineService {
//    @Autowired
//    private PrescriptionMedicineRepository prescriptionMedicineRepository;
//    @Autowired
//    private MedicineRepository medicineRepository;
//    @Autowired
//    private PrescriptionRepository prescriptionRepository;
//    @Autowired
//    private PrescriptionMedicineMapper prescriptionMedicineMapper;
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public PrescriptionMedicineResponse create(PrescriptionMedicineRequest request){
//        Medicine medicine = medicineRepository.findById(request.getMedicine())
//                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Medicine not found", "prescriptionMedicine-e-01"));
//        Prescription prescription = prescriptionRepository.findById(request.getPrescription())
//                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Prescription not found", "prescription-e-02"));
//
//        PrescriptionMedicine prescriptionMedicine = PrescriptionMedicine.builder()
//                .prescription(prescription)
//                .medicine(medicine)
//                .build();
//        return prescriptionMedicineMapper.toPrescriptionMedicineResponse(prescriptionMedicineRepository.save(prescriptionMedicine));
//    }
//    @PreAuthorize("hasRole('ADMIN')")
//    public void delete(String id){
//        PrescriptionMedicine prescriptionMedicine = prescriptionMedicineRepository.findById(id)
//                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Prescription medicine not found", "prescription-e-03"));
//        prescriptionMedicineRepository.delete(prescriptionMedicine);
//    }
//
//}
