package futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.DapAn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.DapAn.DapAnDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.NhapDapAn.Hand.MakeActivity;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class DanhSachDapAn extends AppCompatActivity {
    private ListView lv;
    private ProgressBar prg;
    private TextView tv;
    private ArrayList<DapAn> dsMaDe;
    public DapAnAdapter key_adapter;
    private Toolbar toolbar;
    private BaiThi baiThi;
    public DapAnDatabase dapAnDatabase;
    public static final int RESULT_CODE = 0x33;
    public static final int DELETED_CODE = 0x44;
    public DapAn currentDapAn;

    // Sự kiện khi người dùng nhấn nút mũi tên quay lại
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_by_hand) // Lựa chọn nhập bằng tay
        {
            NhapMaDe();
        } else if (id == R.id.add_by_camera) // Lựa chọn nhập bằng camera
        {
            Toast.makeText(this, "Nhập bằng máy ảnh chưa khả dụng!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.add_by_excel) // Lựa chọn nhập bằng file excels
        {
            Toast.makeText(this, "Nhập bằng excel chưa khả dụng!", Toast.LENGTH_SHORT).show();
        } else if (id == 16908332) {
            onBackPressed();
            finish();
        } else {
            Toast.makeText(this, "Xuất hiện lựa chọn lỗi!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ds_dapan_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_dap_an);

        //region Các xử lý trên toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Đáp án");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        //endregion

        Bundle tempExtras = getIntent().getExtras();
        if (tempExtras != null) {
            baiThi = (BaiThi) getIntent().getSerializableExtra("BaiThi");
        }

        lv = (ListView) findViewById(R.id.dsMaDe);
        prg = (ProgressBar) findViewById(R.id.prgBarMade);
        tv = (TextView) findViewById(R.id.NoRecordMade);

        dapAnDatabase = new DapAnDatabase(this);
        prg.setVisibility(View.VISIBLE);

        dsMaDe = new ArrayList();
        new Thread() {
            @Override
            public void run() {
                // Lấy danh sách mã đề từ csdl
                dsMaDe = dapAnDatabase.TatCaDapAn(baiThi);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            key_adapter = new DapAnAdapter(dsMaDe, DanhSachDapAn.this);
                            lv.setAdapter(key_adapter);
                            prg.setVisibility(View.INVISIBLE);
                            if (lv.getCount() > 0)
                                tv.setVisibility(View.INVISIBLE);
                            else
                                tv.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                        }
                    }
                });

            }
        }.start();
        // Khi người ta chỉ đơn thuần nhấn nhẹ vào một đáp án của đề nào đó thì
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent view_Dap_an = new Intent(DanhSachDapAn.this, MakeActivity.class);
                view_Dap_an.putExtra("BaiThi", baiThi);
                view_Dap_an.putExtra("MaDe", dsMaDe.get(i).getMaDe());
                view_Dap_an.putExtra("Review", true);
                view_Dap_an.putExtra("ID_MaDe",dsMaDe.get(i).getID());
                currentDapAn = dsMaDe.get(i);
                startActivityForResult(view_Dap_an, DELETED_CODE);
            }
        });
        // Khi người dùng nhấn giữ vào trong một đề thi nào đó thì hiển thị một option nhỏ cho người ta
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos = i;
                // Tạo đối tượng thể hiện của đối tượng POPUP menu
                PopupMenu popupMenu = new PopupMenu(DanhSachDapAn.this, view);
                // Đưa giao diện từ tệp xml vào
                popupMenu.getMenuInflater().inflate(R.menu.dapan_option_click, popupMenu.getMenu());
                // Đăng ký cửa sổ bật lên với OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dapan_click_delete: {
                                AlertDialog.Builder hoi = new AlertDialog.Builder(DanhSachDapAn.this);
                                hoi.setCancelable(false)
                                        .setTitle("XÓA ĐÁP ÁN NÀY?")
                                        .setMessage("Thực sự xóa đáp án của mã đề [" + dsMaDe.get(pos).getMaDe()
                                                + "] thuộc bài thi tên [" + baiThi.getTen() + "] hay không?")
                                        .setPositiveButton("Giữ", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (dapAnDatabase.delete(Integer.parseInt(dsMaDe.get(pos).getID())) != -1) {
                                                    key_adapter.remove(dsMaDe.get(pos));
                                                    key_adapter.notifyDataSetChanged();
                                                    if (key_adapter.getCount() <= 0)
                                                        tv.setVisibility(View.VISIBLE);
                                                    Toast.makeText(DanhSachDapAn.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(DanhSachDapAn.this, "Xóa không thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                hoi.create();
                                hoi.show();
                            }
                            return true;
                            default:
                                Toast.makeText(DanhSachDapAn.this, "Lựa chọn lỗi!", Toast.LENGTH_SHORT).show();
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    private void NhapMaDe() {
        LayoutInflater inflater = LayoutInflater.from(DanhSachDapAn.this);
        View v = inflater.inflate(R.layout.nhap_ma_de, null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DanhSachDapAn.this);
        builder.setView(v);

        final Spinner o1 = (Spinner) v.findViewById(R.id.made_num1);
        final Spinner o2 = (Spinner) v.findViewById(R.id.made_num2);
        final Spinner o3 = (Spinner) v.findViewById(R.id.made_num3);

        String[] So = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter(DanhSachDapAn.this,
                android.R.layout.simple_spinner_dropdown_item, So);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        o1.setAdapter(stringArrayAdapter);
        o2.setAdapter(stringArrayAdapter);
        o3.setAdapter(stringArrayAdapter);

        builder.setCancelable(false)
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (o1.getSelectedItemPosition() == -1 ||
                                o2.getSelectedItemPosition() == -1 ||
                                o3.getSelectedItemPosition() == -1) {
                            Toast.makeText(DanhSachDapAn.this, "Mã đề không hợp lệ! Vui lòng thao tác lại.", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        } else {
                            Intent create_by_hand = new Intent(DanhSachDapAn.this, MakeActivity.class);
                            create_by_hand.putExtra("BaiThi", baiThi);
                            create_by_hand.putExtra("MaDe", o1.getSelectedItem().toString() +
                                    o2.getSelectedItem().toString() +
                                    o3.getSelectedItem().toString());
                            create_by_hand.putExtra("Review", false);
                            startActivityForResult(create_by_hand, RESULT_CODE);
                        }
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        // Không làm gì cả!
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == requestCode && (requestCode == RESULT_CODE || requestCode == DELETED_CODE)) {
            String RecivedData = data.getData().toString().replace("\n","");
            String[] Decrypt = RecivedData.split("@@");
            for (int i = 0; i < Decrypt.length; i++) {
                try {
                    Decrypt[i] = new String(Base64.decode(Decrypt[i], Base64.DEFAULT), "UTF-8");
                } catch (Exception e) {
                    Decrypt[i] = "---";
                 }
            }
            DapAn currentDecryptedDapAn = new DapAn(Decrypt[0], Decrypt[1], Decrypt[2], Decrypt[3]);
            if (Decrypt[4].contains("false"))
            {
                key_adapter.add(currentDecryptedDapAn);
                tv.setVisibility(View.INVISIBLE);
            }
            else if (Decrypt[4].contains("true") && currentDapAn.getID().equals(currentDecryptedDapAn.getID()))
            {
                key_adapter.remove(currentDapAn);
                if (key_adapter.getCount() <= 0)
                    tv.setVisibility(View.VISIBLE);
            }
            key_adapter.notifyDataSetChanged();
        }
    }
}
