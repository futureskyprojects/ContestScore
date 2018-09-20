package futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.LoaiDiem;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoiAdapter;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.PhieuTraLoi.PhieuTraLoiDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class ThongKeLoaiDiem extends Fragment {
    BaiThi baiThi;
    int DIEM_CODE;
    LoaiDiemAdapter loaiDiemAdapter;
    ArrayList<LoaiDiem> dsCacLoaiDiemDaChia;
    TextView empty;
    @Override
    public void setArguments(@Nullable Bundle args) {
        baiThi = (BaiThi) args.getSerializable("BaiThi");
        DIEM_CODE = (Integer) args.getInt("DIEM_CODE");
        super.setArguments(args);
    }

    public static ThongKeLoaiDiem newInstance() {
        ThongKeLoaiDiem fragment = new ThongKeLoaiDiem();
        return fragment;
    }

    ArrayList<ArrayList<PhieuTraLoi>> dsCacLoaiDiem;

    public ThongKeLoaiDiem() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ArrayList<LoaiDiem> SepareKindOfScore() {
        ArrayList<PhieuTraLoi> dsPhieuTraLoi = new PhieuTraLoiDatabase(getContext()).TatCaPTL(baiThi);
        for (PhieuTraLoi ptl : dsPhieuTraLoi) {
            boolean added = false;
            for (int i = 0; i < dsCacLoaiDiem.size(); i++) {
                if (dsCacLoaiDiem.get(i).get(0).getDiem() == ptl.getDiem()) {
                    dsCacLoaiDiem.get(i).add(ptl);
                    added = true;
                    break;
                }
            }
            if (!added) {
                ArrayList<PhieuTraLoi> newList = new ArrayList();
                newList.add(ptl);
                dsCacLoaiDiem.add(newList);
            }
        }
        ArrayList<LoaiDiem> dsCacLoaiDiemResult = new ArrayList<>();
        for (ArrayList<PhieuTraLoi> dsPtl : dsCacLoaiDiem)
            dsCacLoaiDiemResult.add(new LoaiDiem(dsPtl, getContext(), DIEM_CODE));
        return dsCacLoaiDiemResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_thong_ke_loai_diem, container, false);
        dsCacLoaiDiem = new ArrayList();
        ListView dsShow = (ListView) myView.findViewById(R.id.ListOfScoreKind);
        empty = (TextView) myView.findViewById(R.id.EmptyNotification);
        setUpListView(dsShow);
        return myView;
    }
    void setUpListView(final ListView listView) {
        new Thread() {
            @Override
            public void run() {
                try
                {
                    dsCacLoaiDiemDaChia = SepareKindOfScore();
                    loaiDiemAdapter = new LoaiDiemAdapter(getContext(),dsCacLoaiDiemDaChia);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(loaiDiemAdapter);
                            if (listView.getCount() > 0)
                                empty.setVisibility(View.GONE);
                            else
                                empty.setVisibility(View.VISIBLE);
                        }
                    });
                }
                catch (Exception e)
                {
                }
            }
        }.start();

    }
}
