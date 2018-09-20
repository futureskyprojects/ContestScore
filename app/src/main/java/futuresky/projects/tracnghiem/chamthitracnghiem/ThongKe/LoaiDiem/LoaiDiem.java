package futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.LoaiDiem;

import android.content.Context;

import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.ScoreRank;

public class LoaiDiem {
    public LoaiDiem(ArrayList<PhieuTraLoi> dsPhieuTraLoi, Context myContext, int DIEM_CODE)
    {
        PhieuTraLoi temp = dsPhieuTraLoi.get(0);
        temp.setTempContext(myContext);
        this.TenLoaiDiem = temp.getDiem(DIEM_CODE);
        this.dsPhieuTraLoi = dsPhieuTraLoi;
    }
    public String TenLoaiDiem;
    public ArrayList <PhieuTraLoi> dsPhieuTraLoi;
}
