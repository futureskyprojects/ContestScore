package futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.CustomView.ViewResultScore;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.DapAn.DapAn;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.DapAn.DapAnDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.PhieuTraLoi.PhieuTraLoiDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.NhapDapAn.Hand.MakeActivity;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;
import futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.LoaiDiem.ThongKeLoaiDiem;
import futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.TiLeTraLoiDung.ThongKeTiLeTraLoiDung;

public class ThongKeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    android.support.v4.app.Fragment selectedFragmednt;
    Toolbar toolbar;
    Menu myMenu;
    BaiThi baiThi;
    int previousId = R.id.tk_phodiem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;
        return setUpMenu(R.id.tk_phodiem);
    }

    boolean setUpMenu(final int keyCode) {
        myMenu.clear();
        final String[] hediem = new String[]{"Hệ 10", "Hệ 4", "Điểm chữ"};
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.thongke_top_menu_loaidiem, myMenu);
        MenuItem item = myMenu.findItem(R.id.thongke_top_menu_loaidiem_spinner);
        item.setCheckable(true);
        final Spinner mySpinner = (Spinner) item.getActionView();
        final ArrayList<String> myList = new ArrayList<>();
        item.setVisible(true);
        switch (keyCode) {
            case R.id.tk_loaidiem:
                for (String option : hediem)
                    myList.add(option);
                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        final Bundle myBundle = new Bundle();
                        myBundle.putSerializable("BaiThi", baiThi);
                        selectedFragmednt = ThongKeLoaiDiem.newInstance();
                        if (myList.get(i).equals(hediem[0]))
                        {
                            myBundle.putInt("DIEM_CODE",PhieuTraLoi.THANGDIEMMUOI_CODE);
                        }
                        else if (myList.get(i).equals(hediem[1]))
                        {
                            myBundle.putInt("DIEM_CODE",PhieuTraLoi.THANGDIEMBON_CODE);
                        }
                        else if (myList.get(i).equals(hediem[2]))
                        {
                            myBundle.putInt("DIEM_CODE",PhieuTraLoi.THANGDIEMCHU_CODE);
                        }
                        selectedFragmednt.setArguments(myBundle);
                        CallFragment(selectedFragmednt);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                break;
            case R.id.tk_tile_traloi_dung:
                selectedFragmednt = ThongKeTiLeTraLoiDung.newInstance();
                ArrayList<DapAn> dsDapAn = new DapAnDatabase(ThongKeActivity.this).TatCaDapAn(baiThi);
                for (DapAn made : dsDapAn)
                    myList.add(made.getMaDe());
                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        final Bundle myBundle = new Bundle();
                        myBundle.putSerializable("BaiThi", baiThi);
                        myBundle.putString("MaDe", mySpinner.getSelectedItem().toString());
                        selectedFragmednt = ThongKeTiLeTraLoiDung.newInstance();
                        selectedFragmednt.setArguments(myBundle);
                        CallFragment(selectedFragmednt);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                break;
            default:
                item.setVisible(false);
                selectedFragmednt = ThongKePhoDiem.newInstance();
                break;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, myList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        mySpinner.setAdapter(adapter);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Tên " + item.getItemId(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case MakeActivity.BackButton:
                onBackPressed();
                finish();
                break;
            default:
                Toast.makeText(this, "Xin lỗi! Lựa chọn không phù hợp!", Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        //region Khu vực set toolbar
        toolbar = (Toolbar) findViewById(R.id.thongke_top_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        //endregion

        //region Khu vực lấy tham biến
        if (getIntent().getExtras() != null) {
            baiThi = (BaiThi) getIntent().getSerializableExtra("BaiThi");
            toolbar.setTitle(baiThi.getTen());
        } else {
            AlertDialog.Builder mbox = new AlertDialog.Builder(this)
                    .setTitle("KHÔNG XÁC ĐỊNH")
                    .setMessage("Xin lỗi quý Thầy/Cô! Hiện tại hệ thống không nhận được bài thi đã chọn, mong quý Thầy/Cô vui lòng thử lại.")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                            finish();
                            return;
                        }
                    });
            mbox.create();
            mbox.show();
        }
        //endregion

        //region Khu vực của bottomNavigationView event
        final Bundle myBundle = new Bundle();
        myBundle.putSerializable("BaiThi", baiThi);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        selectedFragmednt = ThongKePhoDiem.newInstance();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == previousId)
                    return false;
                switch (item.getItemId()) {
                    case R.id.tk_loaidiem:
                        selectedFragmednt = ThongKeLoaiDiem.newInstance();
                        myBundle.putInt("DIEM_CODE",PhieuTraLoi.THANGDIEMMUOI_CODE);
                        break;
                    case R.id.tk_tile_traloi_dung:
                        selectedFragmednt = ThongKeTiLeTraLoiDung.newInstance();
                        break;
                    default:
                        selectedFragmednt = ThongKePhoDiem.newInstance();
                        break;
                }
                selectedFragmednt.setArguments(myBundle);
                CallFragment(selectedFragmednt);
                previousId = item.getItemId();
                return setUpMenu(item.getItemId());
            }
        });
        selectedFragmednt.setArguments(myBundle);
        CallFragment(selectedFragmednt);
        //endregion
    }

    void CallFragment(android.support.v4.app.Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tk_frame, selectedFragment);
        transaction.commit();
    }

    public void ShowReview(int id)
    {
        PhieuTraLoi myPhieuTraLoi = new PhieuTraLoiDatabase(ThongKeActivity.this).LayPhieuTraLoi(id);
        Intent ReView = new Intent(ThongKeActivity.this, ViewResultScore.class);
        ReView.putExtra("PhieuTraLoi", myPhieuTraLoi);
        ReView.putExtra("isReview", true);
        ReView.putExtra("ThongKe", true);
        startActivity(ReView);
    }
}
