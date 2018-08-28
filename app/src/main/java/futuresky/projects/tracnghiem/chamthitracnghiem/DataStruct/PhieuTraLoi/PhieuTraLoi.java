package futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.nio.file.Path;

public class PhieuTraLoi implements Serializable{
    // Trong CSDL
    private String ID;
    private String MaBaiThi; // Dùng để xác định xem bài làm này thuộc bài thi mục nào, lấy ID của BàiThi
    // Phần hiển hị bên ngoài
    private String SBD;
    private byte[] Name_image;
    private float Diem;
    // Phần chi tiết cụ thể
    private String MaDe;
    private int SoCauDung;
    private int SoCauCuaBaiThi;
    private byte[] PTL_image;

    // Các constructor
    public PhieuTraLoi() // Nếu chỉ khai báo tạm và chưa biết làm gì
    {
    }

    // Dành cho việc nhập dữ liệu hoàn toàn từ chuỗi
    public PhieuTraLoi(String ID, String MaBaiThi, String SBD, byte[] Name_image, String Diem, String MaDe, String SoCauDung, String SoCauCuaBaiThi, byte[] PTL_image) {
        this.ID = ID;
        this.MaBaiThi = MaBaiThi;
        this.SBD = SBD;
        this.Name_image = Name_image;
        this.Diem = Float.parseFloat(Diem);
        this.MaDe = MaDe;
        this.SoCauDung = Integer.parseInt(SoCauDung);
        this.SoCauCuaBaiThi = Integer.parseInt(SoCauCuaBaiThi);
        this.PTL_image = PTL_image;
    }

    // Dành cho việc nhập dữ liệu chuẩn
    public PhieuTraLoi(String ID, String MaBaiThi, String SBD, byte[] Name_image, float Diem, String MaDe, int SoCauDung, int SoCauCuaBaiThi, byte[] PTL_image) {
        this.ID = ID;
        this.MaBaiThi = MaBaiThi;
        this.SBD = SBD;
        this.Name_image = Name_image;
        this.Diem = Diem;
        this.MaDe = MaDe;
        this.SoCauDung = SoCauDung;
        this.SoCauCuaBaiThi = SoCauCuaBaiThi;
        this.PTL_image = PTL_image;
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
        this.MaBaiThi = maBaiThi;
    }

    public String getSBD() {
        return SBD;
    }

    public void setSBD(String SBD) {
        this.SBD = SBD;
    }

    public byte[] getName_image() {
        return Name_image;
    }

    public void setName_image(byte[] Name_image) {
        this.Name_image = Name_image;
    }

    public float getDiem() {
        return Diem;
    }

    public void setDiem(float diem) {
        this.Diem = diem;
    }

    public String getMaDe() {
        return MaDe;
    }

    public void setMaDe(String maDe) {
        this.MaDe = maDe;
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

    public byte[] getPTL_image() {
        return PTL_image;
    }

    public void setPTL_image(byte[] PTL_image) {
        this.PTL_image = PTL_image;
    }
    public Bitmap getImageOfPTL()
    {
        return BitmapFactory.decodeByteArray(PTL_image,0,PTL_image.length);
    }
    public Bitmap getImageOfName()
    {
        return BitmapFactory.decodeByteArray(Name_image,0,Name_image.length);
    }
}
