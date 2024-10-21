package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.medicine.MedicineRequest;
import org.telemedicine.server.dto.medicine.MedicineResponse;
import org.telemedicine.server.entity.Drug;
import org.telemedicine.server.entity.Medicine;
import org.telemedicine.server.entity.PrescriptionMedicine;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.MedicineMapper;
import org.telemedicine.server.repository.DrugsRepository;
import org.telemedicine.server.repository.MedicineRepository;
import org.telemedicine.server.repository.PrescriptionMedicineRepository;

import java.util.List;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineMapper medicineMapper;
    @Autowired
    private DrugsRepository drugsRepository;
    @Autowired
    private PrescriptionMedicineRepository prescriptionMedicineRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public MedicineResponse addMedicine(MedicineRequest medicineRequest) {
        if(medicineRepository.existsByName(medicineRequest.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Medicine already exists", "medicine-e-01");
        }
        Medicine medicine = Medicine.builder()
                .name(medicineRequest.getName())
                .chiDinh(medicineRequest.getChiDinh())
                .chongChiDinh(medicineRequest.getChongChiDinh())
                .chuYKhiSUDung(medicineRequest.getChuYKhiSUDung())
                .dangBaoChe(medicineRequest.getDangBaoChe())
                .lieu_cachDung(medicineRequest.getLieu_cachDung())
                .nhomThuoc_tacDung(medicineRequest.getNhomThuoc_tacDung())
                .tacDungKMongMuon(medicineRequest.getTacDungKMongMuon())
                .taiLieuThamKhao(medicineRequest.getTaiLieuThamKhao())
                .thanTrong(medicineRequest.getThanTrong())
                .build();
        Drug drug = drugsRepository.findById(medicineRequest.getDrug())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Drug not found", "medicine-e-02"));
        medicine.setDrug(drug);
        return medicineMapper.toMedicineResponse(medicineRepository.save(medicine));
    }
    public List<MedicineResponse> getAllMedicinesByDrugId(String id) {
        drugsRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Drug not found", "medicine-e-03"));
        return medicineMapper.toMedicineResponseList(medicineRepository.findByDrugId(id));
    }
    public MedicineResponse getMedicineById(String id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(()-> new AppException(HttpStatus.BAD_REQUEST, "Medicine not found", "medicine-e-04"));
        return medicineMapper.toMedicineResponse(medicine);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public MedicineResponse updateMedicine(String id, MedicineRequest medicineRequest) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(()-> new AppException(HttpStatus.BAD_REQUEST, "Medicine not found", "medicine-e-05"));
        medicine.setName(medicineRequest.getName());
        medicine.setChiDinh(medicineRequest.getChiDinh());
        medicine.setChongChiDinh(medicineRequest.getChongChiDinh());
        medicine.setDangBaoChe(medicineRequest.getDangBaoChe());
        medicine.setChuYKhiSUDung(medicineRequest.getChuYKhiSUDung());
        medicine.setLieu_cachDung(medicineRequest.getLieu_cachDung());
        medicine.setNhomThuoc_tacDung(medicineRequest.getNhomThuoc_tacDung());
        medicine.setThanTrong(medicineRequest.getThanTrong());
        medicine.setTacDungKMongMuon(medicineRequest.getTacDungKMongMuon());
        medicine.setTaiLieuThamKhao(medicineRequest.getTaiLieuThamKhao());

        Drug drug = drugsRepository.findById(medicineRequest.getDrug())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Drug not found", "medicine-e-02"));
        medicine.setDrug(drug);

        return medicineMapper.toMedicineResponse(medicineRepository.save(medicine));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMedicine(String id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(()-> new AppException(HttpStatus.BAD_REQUEST, "Medicine not found", "medicine-e-06"));
        List<PrescriptionMedicine> prescriptionMedicines = prescriptionMedicineRepository.findByMedicine(medicine);
        if(!prescriptionMedicines.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Không thể xóa thuốc này", "medicine-e-07");
        }
        medicineRepository.delete(medicine);
    }
}
