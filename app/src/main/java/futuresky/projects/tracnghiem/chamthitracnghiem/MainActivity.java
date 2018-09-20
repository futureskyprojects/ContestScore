package futuresky.projects.tracnghiem.chamthitracnghiem;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.DsBaiThiFrg;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.BaiThi.BaiThiDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.MauPhieu.MauPhieuFragment;
import futuresky.projects.tracnghiem.chamthitracnghiem.Setting.Setting;
import futuresky.projects.tracnghiem.chamthitracnghiem.Setting.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    private Fragment DsBaiThi;
    private Fragment MauPhieu;
    private Toolbar toolbar;
    private BaiThiDatabase DuLieu;
    NavigationView navigationView;
    public final static int CAMERA_PERMISSION = 0xA0;
    public final static int READ_EXTERNAL_STORAGE = 0xA1;
    public final static int WRITE_EXTERNAL_STORAGE = 0xA2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hỏi quyền
        HoiQuyen();
        // Kê khai sử dụng Thao tác trên CSDL bài thi
        DuLieu = new BaiThiDatabase(getApplicationContext());
        //region Float Button
        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton) findViewById(R.id.activity_main_rfab);
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getApplicationContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Thêm bài thi")
                .setResId(R.drawable.addtest)
                .setIconNormalColor(0xff2ecc71)
                .setIconPressedColor(0xff1abc9c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Xóa tất cả")
                .setResId(R.drawable.ic_deleteall)
                .setIconNormalColor(0xffe74c3c)
                .setIconPressedColor(0xffe67e22)
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getApplicationContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
        //endregion
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        MauPhieu = new MauPhieuFragment();
        DsBaiThi = new DsBaiThiFrg();
        setFragment(DsBaiThi);
        toolbar.setTitle("Danh sách bài thi");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    int previous_selected = R.id.nav_ds;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ds && previous_selected != id) {
            rfaLayout.setVisibility(View.VISIBLE);
            previous_selected = id;
            DsBaiThi = new DsBaiThiFrg();
            setFragment(DsBaiThi);
            toolbar.setTitle("Danh sách bài thi");
        } else if (id == R.id.nav_giay && previous_selected != id) {
            previous_selected = id;
            rfaLayout.setVisibility(View.INVISIBLE);
            toolbar.setTitle("Mẫu phiếu");
            setFragment(MauPhieu);
        } else if (id == R.id.nav_hd && previous_selected != id) {
            previous_selected = id;
            rfaLayout.setVisibility(View.INVISIBLE);
            toolbar.setTitle("Hướng dẫn");
        } else if (id == R.id.nav_caidat && previous_selected != id) {
            Intent mySetting = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(mySetting);
            item.setChecked(false);
            navigationView.getMenu().getItem(0).setChecked(true);
        } else if (id == R.id.nav_share && previous_selected != id) {
            Toast.makeText(this, "Chia sẻ ứng dụng ngay!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_fb && previous_selected != id) {
            Toast.makeText(this, "Đang đến Facebook tác giả...", Toast.LENGTH_SHORT).show();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Setting.applicationAuthorFacebook));
            startActivity(browserIntent);
        } else if (id == R.id.nav_send && previous_selected != id) {
            Toast.makeText(this, "Mở email để gửi phản hồi!", Toast.LENGTH_SHORT).show();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", Setting.applicationAuthorEmail, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi về dự án phần mềm chấm thi trắc nghiệm trên điện thoại dùng hệ điều hành Android");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Nhập nội dung phản hồi của bạn vào đây");
            startActivity(Intent.createChooser(emailIntent, "Gửi phản hồi"));
        } else if (id == R.id.nav_exit && previous_selected != id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("THOÁT?");
            builder.setMessage("Thực sự muốn thoát ứng dụng ngay?");
            builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Đã hủy thao tác Thoát!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.create();
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        doBtn(position);
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        doBtn(position);
    }

    void doBtn(int position) {
        if (position == 0) {
            // Dành cho  Thêm bài thi
            show();

        } else if (position == 1) {
            // Dành cho xóa tất cả
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("XÓA TẤT CẢ?");
            builder.setMessage("Việc này không thể hoàn tác! Thực sự muốn xóa tất cả các bài đã tạo?");
            builder.setPositiveButton("Vẫn xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DuLieu.XoaTatCa();
                    Toast.makeText(MainActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    Update_DS_BaiThi();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Đã hủy tác vụ xóa!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.create();
            builder.show();
        }
        rfabHelper.toggleContent();
    }

    public void show() {
        // get view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.them_bai_thi_dialog, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText mTenBaiThi;
        final Spinner mLoaiGiayThi;
        final EditText mSoCau;
        mTenBaiThi = (EditText) promptView.findViewById(R.id.ten_bai_thi_moi);

        mLoaiGiayThi = (Spinner) promptView.findViewById(R.id.ds_loai_giay);
        String[] loaigiay = new String[]{"20 câu", "40 câu", "60 câu", "100 câu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, loaigiay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLoaiGiayThi.setAdapter(adapter);

        mSoCau = (EditText) promptView.findViewById(R.id.so_cau);

        // set up dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTenBaiThi.length() < 1) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("TÊN BÀI THI RỖNG!")
                                    .setMessage("Phải có tên bài thi để phân biệt!")
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create();
                            builder.show();
                            return;
                        }
                        if (mSoCau.length() < 1) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("SỐ CÂU RỖNG!")
                                    .setMessage("Số câu của đề không thể là giá trị rỗng!")
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create();
                            builder.show();
                            return;
                        }
                        if (Integer.parseInt(mSoCau.getText().toString()) < 1) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("SỐ CÂU KHÔNG ĐÚNG!")
                                    .setMessage("Bài thi phải có ít nhất 1 câu!")
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create();
                            builder.show();
                            return;
                        }
                        if (Integer.parseInt(mSoCau.getText().toString()) >
                                Integer.parseInt(mLoaiGiayThi.getSelectedItem().toString().replace(" câu", ""))) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("KHÔNG HỢP LỆ!")
                                    .setMessage("Số câu của đề không thể lớn hơn số câu giới hạn của giấy thi!")
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create();
                            builder.show();
                            return;
                        }
                        int MAX_ID = DuLieu.MaxID();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date now = new Date();
                        try {
                            Calendar mC = Calendar.getInstance();
                            mC.add(Calendar.MONTH, 1);
                            String dateTimeStr = mC.get(Calendar.DAY_OF_MONTH) + "/" +
                                    mC.get(Calendar.MONTH) + "/" + mC.get(Calendar.YEAR) + " " +
                                    mC.get(Calendar.HOUR_OF_DAY) + ":" +
                                    mC.get(Calendar.MINUTE) + ":00";
                            Log.d("MainActivity Time", dateTimeStr);
                            now = df.parse(dateTimeStr);
                        } catch (ParseException e) {
                            Log.e("MainActivity", "Không chuyển dạng ngày tháng được! " + e.getMessage());
                        }
                        BaiThi bt = new BaiThi(Integer.toString(MAX_ID + 1), mTenBaiThi.getText().toString(),
                                now, Integer.parseInt(mLoaiGiayThi.getSelectedItem().toString().replace(" câu", "")),
                                Integer.parseInt(mSoCau.getText().toString()), 10);
                        DuLieu.ThemBaiThiMoi(bt);
                        Toast.makeText(MainActivity.this, "Đã thêm bài thi!", Toast.LENGTH_SHORT).show();
                        Update_DS_BaiThi();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    private void setFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FrgShow, fragment);
        fragmentTransaction.commit();
    }

    public void Update_DS_BaiThi() {
        DsBaiThi = new DsBaiThiFrg();
        setFragment(DsBaiThi);
    }

    //region Hỏi quyền để sử dụng
    private void HoiQuyen() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
        } else {
            AlertDialog.Builder mbox = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("THÔNG BÁO")
                    .setCancelable(false)
                    .setMessage("Ứng dụng cần một số quyền sau để hoạt động\n" +
                            "1. Ghi vào bộ nhớ: Lưu trữ dữ liệu của ứng dụng.\n" +
                            "2. Đọc dữ liệu: Đọc các bản ghi dữ liệu trước đó.\n" +
                            "3. Máy ảnh: dùng để quét đáp án đề thi hoặc bài làm.\n")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final AlertDialog.Builder mbox2 = new AlertDialog.Builder(MainActivity.this);
                            // Quyền thứ nhất ghi vào bộ nhớ
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Đóng ứng dụng và trở về
                            finish();
                            return;
                        }
                    });
            mbox.create();
            mbox.show();

        }
    }

    //endregion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Đã cấp quyền đọc dữ liệu!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "TỪ CHỐI QUYỀN ĐỌC DỮ LIỆU!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                break;
            case WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Đã cấp quyền ghi dữ liệu!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "TỪ CHỐI QUYỀN GHI DỮ LIỆU!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                break;
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Đã cấp quyền sử dụng máy ảnh!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "TỪ CHỐI QUYỀN SỬ DỤNG MÁY ẢNH! KHÔNG THỂ THỰC HIỆN SCAN BÀI THI", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Không xác định!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
