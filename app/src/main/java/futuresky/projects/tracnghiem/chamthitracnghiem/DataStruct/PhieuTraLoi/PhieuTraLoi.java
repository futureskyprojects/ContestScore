package futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi;

import java.nio.file.Path;

public class PhieuTraLoi {
    // Trong CSDL
    private String ID;
    private String MaBaiThi; // Dùng để xác định xem bài làm này thuộc bài thi mục nào, lấy ID của BàiThi
    // Phần hiển hị bên ngoài
    private String SBD;
    private String PathToName;
    private float Diem;
    // Phần chi tiết cụ thể
    private String MaDe;
    private int SoCauDung;
    private int SoCauCuaBaiThi;
    private String PathToPhieuTraLoi;

    // Các constructor
    public PhieuTraLoi() // Nếu chỉ khai báo tạm và chưa biết làm gì
    {
    }

    // Dành cho việc nhập dữ liệu hoàn toàn từ chuỗi
    public PhieuTraLoi(String ID, String MaBaiThi, String SBD, String PathToName, String Diem, String MaDe, String SoCauDung, String SoCauCuaBaiThi, String PathToPTL) {
        this.ID = ID;
        this.MaBaiThi = MaBaiThi;
        this.SBD = SBD;
        this.PathToName = PathToName;
        this.Diem = Float.parseFloat(Diem);
        this.MaDe = MaDe;
        this.SoCauDung = Integer.parseInt(SoCauDung);
        this.SoCauCuaBaiThi = Integer.parseInt(SoCauCuaBaiThi);
        this.PathToPhieuTraLoi = PathToPTL;
    }

    // Dành cho việc nhập dữ liệu chuẩn
    public PhieuTraLoi(String ID, String MaBaiThi, String SBD, String PathToName, float Diem, String MaDe, int SoCauDung, int SoCauCuaBaiThi, String PathToPTL) {
        this.ID = ID;
        this.MaBaiThi = MaBaiThi;
        this.SBD = SBD;
        this.PathToName = PathToName;
        this.Diem = Diem;
        this.MaDe = MaDe;
        this.SoCauDung = SoCauDung;
        this.SoCauCuaBaiThi = SoCauCuaBaiThi;
        this.PathToPhieuTraLoi = PathToPTL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMaBaiThi() {
        return MaBaiThi;
    }

    public void setMaBaiThi(String maBaiThi) {
        MaBaiThi = maBaiThi;
    }

    public String getSBD() {
        return SBD;
    }

    public void setSBD(String SBD) {
        this.SBD = SBD;
    }

    public String getPathToName() {
        return PathToName;
    }

    public void setPathToName(String pathToName) {
        PathToName = pathToName;
    }

    public float getDiem() {
        return Diem;
    }

    public void setDiem(float diem) {
        Diem = diem;
    }

    public String getMaDe() {
        return MaDe;
    }

    public void setMaDe(String maDe) {
        MaDe = maDe;
    }

    public int getSoCauDung() {
        return SoCauDung;
    }

    public void setSoCauDung(int soCauDung) {
        SoCauDung = soCauDung;
    }

    public int getSoCauCuaBaiThi() {
        return SoCauCuaBaiThi;
    }

    public void setSoCauCuaBaiThi(int soCauCuaBaiThi) {
        SoCauCuaBaiThi = soCauCuaBaiThi;
    }

    public String getPathToPhieuTraLoi() {
        return PathToPhieuTraLoi;
    }

    public void setPathToPhieuTraLoi(String pathToPhieuTraLoi) {
        PathToPhieuTraLoi = pathToPhieuTraLoi;
    }
}
