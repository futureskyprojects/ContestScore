package futuresky.projects.tracnghiem.chamthitracnghiem.MauPhieu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.text.pdf.parser.Line;

import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class MauPhieuFragment extends Fragment {
    private LinearLayout m20_xemtruoc;
    private LinearLayout m20_luu;
    private LinearLayout m20_open;
    private LinearLayout m40_xemtruoc;
    private LinearLayout m40_luu;
    private LinearLayout m40_open;
    private LinearLayout m60_xemtruoc;
    private LinearLayout m60_luu;
    private LinearLayout m60_open;
    private LinearLayout m100_xemtruoc;
    private LinearLayout m100_luu;
    private LinearLayout m100_open;

    private final static int PREVIEW_CODE = 0x01;
    private final static int SAVE_CODE = 0x02;
    private final static int OPEN_CODE = 0x03;

    public MauPhieuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_mau_phieu, container, false);
        m20_xemtruoc = (LinearLayout) myView.findViewById(R.id.m20_xemtruoc);
        m20_xemtruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(20, PREVIEW_CODE);
            }
        });

        m20_luu = (LinearLayout) myView.findViewById(R.id.m20_luu);
        m20_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(20, SAVE_CODE);
            }
        });

        m20_open = (LinearLayout) myView.findViewById(R.id.m20_open);
        m20_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(20, OPEN_CODE);
            }
        });

        m40_xemtruoc = (LinearLayout) myView.findViewById(R.id.m40_xemtruoc);
        m40_xemtruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(40, PREVIEW_CODE);
            }
        });

        m40_luu = (LinearLayout) myView.findViewById(R.id.m40_luu);
        m40_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(40, SAVE_CODE);
            }
        });

        m40_open = (LinearLayout) myView.findViewById(R.id.m40_open);
        m40_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(40, OPEN_CODE);
            }
        });

        m60_xemtruoc = (LinearLayout) myView.findViewById(R.id.m60_xemtruoc);
        m60_xemtruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(60, PREVIEW_CODE);
            }
        });

        m60_luu = (LinearLayout) myView.findViewById(R.id.m60_luu);
        m60_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(60, SAVE_CODE);
            }
        });

        m60_open = (LinearLayout) myView.findViewById(R.id.m60_open);
        m60_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(60, OPEN_CODE);
            }
        });

        m100_xemtruoc = (LinearLayout) myView.findViewById(R.id.m100_xemtruoc);
        m100_xemtruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(100, PREVIEW_CODE);
            }
        });

        m100_luu = (LinearLayout) myView.findViewById(R.id.m100_luu);
        m100_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(100, SAVE_CODE);
            }
        });

        m100_open = (LinearLayout) myView.findViewById(R.id.m100_open);
        m100_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunEvent(100, OPEN_CODE);
            }
        });
        return myView;
    }

    void RunEvent(int paperType, int actionCode) {
        String filePath = "" + paperType;
        String urlPath = "" + paperType;
        Toast.makeText(getContext(), "Bạn vừa nhấn chọn loại phiếu " + paperType + " câu với mã hành động là " + actionCode, Toast.LENGTH_SHORT).show();
        switch (actionCode) {
            case PREVIEW_CODE:
                return;
            case SAVE_CODE:
                return;
            case OPEN_CODE:
                return;
            default:
                return;
        }
    }
}
