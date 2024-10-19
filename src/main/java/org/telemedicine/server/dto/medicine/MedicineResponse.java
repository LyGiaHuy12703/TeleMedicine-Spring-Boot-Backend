package org.telemedicine.server.dto.medicine;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Drug;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineResponse {
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
    Drug drug;
}
