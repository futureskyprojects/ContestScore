package futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.LoaiDiem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.CustomView.ViewResultScore;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoiAdapter;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;
import futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.ThongKeActivity;

public class LoaiDiemAdapter extends ArrayAdapter<LoaiDiem> implements OnClickListener {
    ArrayList<LoaiDiem> dsLoaiDiem;
    Context myContext;
    TextView TenLoaiDiem;
    TextView SoLuongBai;
    ImageView ButtonX;
    boolean isShow = false;
    public LoaiDiemAdapter(Context myContext, ArrayList<LoaiDiem> dsLoaiDiem)
    {
        super(myContext, R.layout.scorekind_row, dsLoaiDiem);
        this.dsLoaiDiem = dsLoaiDiem;
        this.myContext = myContext;
    }
    @Override
    public LoaiDiem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater infilate = LayoutInflater.from(myContext);
        convertView = infilate.inflate(R.layout.scorekind_row,parent,false);
        TenLoaiDiem = (TextView) convertView.findViewById(R.id.NameOfScoreKind);
        SoLuongBai = (TextView) convertView.findViewById(R.id.NumberOfContestOfThisScoreKind);
        ButtonX = (ImageView) convertView.findViewById(R.id.ScoreKindButton);

        TenLoaiDiem.setText("Điểm " + dsLoaiDiem.get(position).TenLoaiDiem);
        SoLuongBai.setText("Hiện có " + dsLoaiDiem.get(position).dsPhieuTraLoi.size() + " bài");
        ButtonX.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                runAction(position);
            }
        });
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                runAction(position);
            }
        });
        return convertView;
    }
    void runAction(final int Pos)
    {
        if (isShow)
            return;
        isShow = true;
        // Create layout
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        View myLayout = layoutInflater.inflate(R.layout.list_of_score_kind, null);
        ListView myListView = myLayout.findViewById(R.id.listOfPerScoreKind);
        // Create Adepter for listview
        PhieuTraLoiAdapter phieuTraLoiAdapter = new PhieuTraLoiAdapter(dsLoaiDiem.get(Pos).dsPhieuTraLoi, myContext);
        myListView.setAdapter(phieuTraLoiAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (myContext instanceof ThongKeActivity) {
                    ((ThongKeActivity) myContext).ShowReview(Integer.parseInt(dsLoaiDiem.get(Pos).dsPhieuTraLoi.get(i).getID()));
                }
            }
        });
        // Create prompt
        AlertDialog.Builder mbox = new AlertDialog.Builder(myContext)
                .setCancelable(false)
            .setView(myLayout)
                .setTitle("SINH VIÊN CÓ ĐIỂM " + dsLoaiDiem.get(Pos).TenLoaiDiem)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isShow = false;
                        dialogInterface.cancel();
                    }
                });
        mbox.create();
        mbox.show();
    }
    @Override
    public void onClick(View view) {

    }
}
