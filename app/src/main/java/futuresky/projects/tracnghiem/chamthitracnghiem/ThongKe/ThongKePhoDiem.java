package futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.PhieuTraLoi.PhieuTraLoiDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThongKePhoDiem extends Fragment {
    PieChart chart;
    BaiThi baiThi;

    public static ThongKePhoDiem newInstance() {
        ThongKePhoDiem fragment = new ThongKePhoDiem();
        return fragment;
    }

    public ThongKePhoDiem() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        baiThi = (BaiThi) args.getSerializable("BaiThi");
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_thong_ke_pho_diem, container, false);
        chart = myView.findViewById(R.id.tk_phodiem_chart);
        addEntry();
        return myView;
    }

    void addEntry() {
        //region Phổ điểm xuất sắc
        int xuatSacCount = 0;
        //endregion

        //region Phổ điểm giỏi
        int gioiCount = 0;
        //endregion

        //region Phổ điểm khá
        int khaCount = 0;
        //endregion

        //region Phổ điểm trung bình
        int trungBinhCount = 0;
        //endregion

        //region Phổ điểm yếu
        int yeuCount = 0;
        //endregion

        //region Phổ điểm kém
        int kemCount = 0;
        //endregion

        ArrayList<PhieuTraLoi> dsPhieuTraLoi = new PhieuTraLoiDatabase(getContext()).TatCaPTL(baiThi);
        for (PhieuTraLoi phieuTraLoi : dsPhieuTraLoi) {
            switch (phieuTraLoi.getRankCode()) {
                case 0:
                    xuatSacCount++;
                    break;
                case 1:
                    gioiCount++;
                    break;
                case 2:
                    khaCount++;
                    break;
                case 3:
                    khaCount++;
                    break;
                case 4:
                    trungBinhCount++;
                    break;
                case 5:
                    yeuCount++;
                    break;
                case 6:
                    kemCount++;
                    break;
            }
        }
        List<PieEntry> entries = new ArrayList();
        // Tạo các bản dữ liệu nhỏ
        if (xuatSacCount != 0)
            entries.add(new PieEntry(xuatSacCount, "Xuất Sắc"));
        if (gioiCount != 0)
            entries.add(new PieEntry(gioiCount, "Giỏi"));
        if (khaCount != 0)
            entries.add(new PieEntry(khaCount, "Khá"));
        if (trungBinhCount != 0)
            entries.add(new PieEntry(trungBinhCount, "Trung Bình"));
        if (yeuCount != 0)
            entries.add(new PieEntry(yeuCount, "Yếu"));
        if (kemCount != 0)
            entries.add(new PieEntry(kemCount, "Kém"));
        entries.add(new PieEntry(0, ""));
        // Đặt các mạnh dữ liệu vào trường lớn của nó
        PieDataSet dataSet = new PieDataSet(entries,"Khác");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(20);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new DefaultValueFormatter(DecimalFormat.INTEGER_FIELD));
        chart.setData(pieData);
        Description myDes = new Description();
        myDes.setTextSize(13);
        myDes.setText("Biểu thị phổ điểm, những phổ điểm trống sẽ bị ẩn.");
        chart.setDescription(myDes);
        chart.setCenterText("BIỂU ĐỒ\nPHỔ ĐIỂM");
        chart.setCenterTextColor(Color.MAGENTA);
        chart.setCenterTextSize(18);
        chart.animateY(1500);
        chart.invalidate();
    }

}
