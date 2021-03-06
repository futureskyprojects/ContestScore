package futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.Database.BaiThi.BaiThiDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;
public class DsBaiThiFrg extends Fragment {

    private ListView dsBaiThi;
    private ArrayList<BaiThi> DanhSaSachBaiThi;
    private BaiThiAdapter baiThiAdapter;


    public DsBaiThiFrg()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_ds_bai_thi_frg, container, false);
        dsBaiThi = (ListView) v.findViewById(R.id.dsBaiThi);
        DanhSaSachBaiThi = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                DanhSaSachBaiThi = new BaiThiDatabase(getContext()).TatCaBaiThi();
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                baiThiAdapter = new BaiThiAdapter(DanhSaSachBaiThi, getContext());
                                dsBaiThi.setAdapter(baiThiAdapter);
                                v.findViewById(R.id.prg).setVisibility(View.INVISIBLE);
                                if (dsBaiThi.getCount() <= 0)
                                    v.findViewById(R.id.NoRecord).setVisibility(View.VISIBLE);
                                else
                                    v.findViewById(R.id.NoRecord).setVisibility(View.INVISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
        }.start();
        return v;
    }

}
