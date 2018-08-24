package futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaiThi implements Serializable {
    private String id;
    private String Ten;
    private Date NgayTao;
    private int LoaiGiay;
    private int SoCau;
    private int HeDiem;

    public BaiThi(String id, String Ten, Date NgayTao, int LoaiGiay, int SoCau, int HeDiem) {
        this.id = id;
        this.Ten = Ten;
        Calendar mC = Calendar.getInstance();
        mC.setTime(NgayTao);
        mC.add(Calendar.MONTH,1);
        String dateTimeStr = mC.get(Calendar.DAY_OF_MONTH) + "/" +
                mC.get(Calendar.MONTH) + "/" + mC.get(Calendar.YEAR) + " " +
                mC.get(Calendar.HOUR_OF_DAY) + ":" +
                mC.get(Calendar.MINUTE) + ":00";
        try {
            this.NgayTao = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeStr);
        } catch (ParseException e) {
            Log.e("BaiThi.java", "Không chuyển từ string sang date được! " + e.getMessage());
        }
        this.LoaiGiay = LoaiGiay;
        this.SoCau = SoCau;
        this.HeDiem = HeDiem;
    }

    public BaiThi(String id, String Ten, String NgayTao, String LoaiGiay, String SoCau, String HeDiem) {
        this.id = id;
        this.Ten = Ten;
        try {
            this.NgayTao = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(NgayTao);
        } catch (ParseException e) {
            this.NgayTao = new Date();
            Log.e("BaiThi.java", "Không chuyển từ string sang date được! " + e.getMessage());
        }
        this.LoaiGiay = Integer.parseInt(LoaiGiay);
        this.SoCau = Integer.parseInt(SoCau);
        this.HeDiem = Integer.parseInt(HeDiem);
    }

    public BaiThi() {
    }

    public String getId() {
        return id;
    }

    public String getTen() {
        return Ten;
    }

    public Date getNgayTao() {
        return NgayTao;
    }

    public int getLoaiGiay() {
        return LoaiGiay;
    }

    public int getSoCau() {
        return SoCau;
    }

    public int getHeDiem() {
        return HeDiem;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public void setNgayTao(Date ngayTao) {
        NgayTao = ngayTao;
    }

    public void setLoaiGiay(int loaiGiay) {
        LoaiGiay = loaiGiay;
    }

    public void setSoCau(int soCau) {
        SoCau = soCau;
    }

    public void setHeDiem(int heDiem) {
        HeDiem = heDiem;
    }
}
