package org.telemedicine.server.enums;

public enum AcademicDegree {
    BAC_SI_Y_KHOA("Bác sĩ y khoa"),
    BAC_SI_CAO_CAP("Bác sĩ cao cấp"),
    BAC_SI_CHUYEN_KHOA_I("Bác sĩ chuyên khoa I"),
    BAC_SI_CHUYEN_KHOA_II("Bác sĩ chuyên khoa II"),
    BAC_SI_NOI_TRU("Bác sĩ nội trú"),
    CU_NHAN("Cử nhân"),
    THAC_SI("Thạc sĩ"),
    TIEN_SI("Tiến sĩ"),
    NHA_NGHIEN_CUU("Nhà nghiên cứu");

    private final String label;

    AcademicDegree(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
