package futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.TiLeTraLoiDung;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.DapAn.DapAnDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.PhieuTraLoi.PhieuTraLoiDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.Processing.ImageProcessing;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThongKeTiLeTraLoiDung extends Fragment {
    ArrayList<PhieuTraLoi> dsPhieuTraLoi;
    String MaDe;
    int[] coutTrue;
    BaiThi baiThi;
    int[] myKey;
    PhieuTraLoiDatabase phieuTraLoiDatabase;
    DapAnDatabase dapAnDatabase;
    ImageProcessing imageProcessing;
    Data1Adapter data1Adapter;
    public static ThongKeTiLeTraLoiDung newInstance() {
        ThongKeTiLeTraLoiDung fragment = new ThongKeTiLeTraLoiDung();
        return fragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        baiThi = (BaiThi) args.getSerializable("BaiThi");
        MaDe = args.getString("MaDe");
        super.setArguments(args);
    }

    public ThongKeTiLeTraLoiDung() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        phieuTraLoiDatabase = new PhieuTraLoiDatabase(getContext());
        dapAnDatabase = new DapAnDatabase(getContext());
        dsPhieuTraLoi = phieuTraLoiDatabase.TatCaPTL(baiThi);
        imageProcessing = new ImageProcessing();
        View myView = inflater.inflate(R.layout.fragment_thong_ke_ti_le_tra_loi_dung, container, false);
        ListView dsTl = myView.findViewById(R.id.thongke_tl_dung);
        data1Adapter = new Data1Adapter(getContext(), Processing());
        dsTl.setAdapter(data1Adapter);
        return myView;
    }

    ArrayList<Data1> Processing()
    {
        int checkId = 0;
        try {
            checkId = dapAnDatabase.LayIdMaDe(Integer.parseInt(imageProcessing.convertMadeToText(dsPhieuTraLoi.get(0).getMaDe())),
                    Integer.parseInt(baiThi.getId()));  // Lấy id của mã đè hiện tại trong csdl
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        coutTrue = new int[baiThi.getLoaiGiay()];
        for (int i = 0; i < coutTrue.length;i++)
            coutTrue[i] = 0;
        myKey = new int[baiThi.getLoaiGiay() + 20];
        for (int i = 0; i < myKey.length; i++)
            myKey[i]=-1;
        myKey = this.imageProcessing.convertKeyToArray(dapAnDatabase.layDapAn(checkId).getDapAn(),
                baiThi.getLoaiGiay(), baiThi.getSoCau());
        for (PhieuTraLoi ptl : dsPhieuTraLoi)
        {
            int [] dsCauTL = new int[myKey.length];
            for (int i = 0; i < dsCauTL.length;i++)
                dsCauTL[1] = -1;
            dsCauTL = imageProcessing.convertStringToArray(ptl.getDS_CauTraLoi());
            int[] trueAnswer = this.imageProcessing.getTrueAnswer(dsCauTL, myKey, baiThi.getSoCau());
            for (int i = 20; i < trueAnswer.length;i++)
            {
                Log.d("VT " + i, dsCauTL[i] + " " + trueAnswer[i]);
                if (trueAnswer[i]==1)
                    coutTrue[i-20]++;
            }
        }
        ArrayList<Data1> dsTiLeDung = new ArrayList();
        for (int i = 20; i < myKey.length; i++)
        {
            Log.d("CAU " + (i-20), coutTrue[i-20] + "");
            dsTiLeDung.add(new Data1(myKey[i], (coutTrue[i-20]/(float)dsPhieuTraLoi.size())));
        }
        return dsTiLeDung;
    }
}
