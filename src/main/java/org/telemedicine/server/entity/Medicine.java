package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NotBlank(message = "Medicine name is required")
    @Size(max = 100, message = "Medicine name cannot exceed 100 characters")
    String name;

    @NotBlank(message = "Dang bao che is required")
    @Size(max = 100, message = "Dang bao che cannot exceed 100 characters")
    String dangBaoChe;

    @NotBlank(message = "Nhom thuoc/tac dung is required")
    @Size(max = 255, message = "Nhom thuoc/tac dung cannot exceed 255 characters")
    String nhomThuoc_tacDung;

    @NotBlank(message = "Chi dinh is required")
    @Size(max = 500, message = "Chi dinh cannot exceed 500 characters")
    String chiDinh;

    @Size(max = 500, message = "Chong chi dinh cannot exceed 500 characters")
    String chongChiDinh;

    @Size(max = 500, message = "Than trong cannot exceed 500 characters")
    String thanTrong;

    @Size(max = 500, message = "Tac dung khong mong muon cannot exceed 500 characters")
    String tacDungKMongMuon;

    @Size(max = 500, message = "Lieu/cach dung cannot exceed 500 characters")
    String lieu_cachDung;

    @Size(max = 500, message = "Chu y khi su dung cannot exceed 500 characters")
    String chuYKhiSUDung;

    @Size(max = 500, message = "Tai lieu tham khao cannot exceed 500 characters")
    String taiLieuThamKhao;

    @ManyToOne
    @JoinColumn(name = "drug_id")
    @JsonBackReference
    Drug drug;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();
}
