package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.drug.DrugRequest;
import org.telemedicine.server.dto.drug.DrugResponse;
import org.telemedicine.server.entity.Drug;
import org.telemedicine.server.entity.Medicine;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.DrugMapper;
import org.telemedicine.server.repository.DrugsRepository;
import org.telemedicine.server.repository.MedicineRepository;

import java.util.List;

@Service
public class DrugsService {
    @Autowired
    private DrugsRepository drugsRepository;
    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private MedicineRepository medicineRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public DrugResponse createDrugs(DrugRequest request) {
        if(drugsRepository.existsByName(request.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST,"Drug already exist", "Drug-e-01");
        }
        Drug drug = drugMapper.toDrug(request);
        drugsRepository.save(drug);
        return drugMapper.toDrugResponse(drug);
    }
    public List<DrugResponse> getAllDrugs() {
        List<Drug> drugs = drugsRepository.findAll();
        return drugMapper.toDrugResponseList(drugs);
    }
    public DrugResponse getDrugById(String id) {
        Drug drug = drugsRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Drug not found", "Drug-e-02"));

        return drugMapper.toDrugResponse(drug);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public DrugResponse updateDrugById(String id, DrugRequest request) {
        Drug drug = drugsRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Drug not found", "Drug-e-02"));

        drug.setName(request.getName());
        drugsRepository.save(drug);
        return drugMapper.toDrugResponse(drug);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDrugById(String id) {
        drugsRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Drug not found", "Drug-e-02"));
        List<Medicine> medicines = medicineRepository.findByDrugId(id);
        if(!medicines.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Không thể xoá loại thuốc ", "drug-e-03");
        }
        drugsRepository.deleteById(id);
    }
}
