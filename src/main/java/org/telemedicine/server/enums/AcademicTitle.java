package org.telemedicine.server.enums;

public enum AcademicTitle {
    GIAO_SU("Giáo sư"),
    PHO_GIAO_SU("Phó giáo sư"),
    NHA_PHAT_TRIEN("Nhà phát triển"),
    KHONG("Không");

    private final String title;

    AcademicTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

