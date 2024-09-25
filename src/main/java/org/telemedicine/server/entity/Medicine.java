package org.telemedicine.server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String dangBaoChe;
    String nhomThuoc_tacDung;
    String chiDinh;
    String chongChiDinh;
    String thanTrong;
    String tacDungKMongMuon;
    String lieu_cachDung;
    String chuYKhiSUDung;
    String taiLieuThamKhao;

    @ManyToOne
    @JoinColumn(name = "drug_id")
    Drug drug;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();

}
