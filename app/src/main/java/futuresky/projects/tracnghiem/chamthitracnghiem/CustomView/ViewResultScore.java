package futuresky.projects.tracnghiem.chamthitracnghiem.CustomView;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.drm.DrmStore;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.core.Mat;
import org.w3c.dom.Text;

import java.util.Random;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.MainCamera;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class ViewResultScore extends AppCompatActivity {

    Toolbar mytoolbar;
    PhieuTraLoi myPTL;
    ImageView resultView;
    TextView ShowScore;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //region Thao tác khi nhấn nút quay lại
        if (id == 16908332) {
            AlertDialog.Builder mbox = new AlertDialog.Builder(ViewResultScore.this)
                    .setTitle("QUÝ THẦY/CÔ MUỐN HỦY?")
                    .setMessage("Hiện tại kết quả chấm của bài làm này chưa được lưu," +
                            "quý Thầy/Cô thực sự muốn hủy kết quả này?")
                    .setPositiveButton("Xem lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setNegativeButton("Đúng vậy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                            finish();
                        }
                    });
            mbox.create();
            mbox.show();
        }
        //endregion

        //region Thao tác khi nhấn nút xem thông tin bài làm
        else if (id == R.id.activity_view_result_score_info) {
            Toast.makeText(this, "Thông tin bài thi hiện chưa khả dụng!", Toast.LENGTH_SHORT).show();
        }
        //endregion

        //region Thao tác khi nhấn nút lưu bài làm
        else if (id == R.id.activity_view_result_score_saveAns) {
            Toast.makeText(this, "Lưu bài thi hiện chưa khả dụng!", Toast.LENGTH_SHORT).show();
        }
        //endregion

        else
            Toast.makeText(this, "Lựa chọn không tồn tại, vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result_score);
        //region Thiết lập thanh toolbar cho activity
        mytoolbar = (Toolbar) findViewById(R.id.activity_view_result_score_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        //endregion

        resultView = (ImageView) findViewById(R.id.activity_view_result_score_imageView);
        resultView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                ShowScorePosition();
            }
        });
        ShowScore = (TextView) findViewById(R.id.activity_view_result_score_showScore);
        //region Phần kiểm tra tham biến
        Bundle myBundle = getIntent().getExtras();
        if (myBundle != null) {
            myPTL = (PhieuTraLoi) getIntent().getSerializableExtra("PhieuTraLoi");
            mytoolbar.setTitle("SBD: " + myPTL.getSBD());
            resultView.setImageBitmap(myPTL.getImageOfPTL());
            Toast.makeText(this, "Điểm: " + myPTL.getDiem(), Toast.LENGTH_SHORT).show();
            ShowScore.setText(Float.toString((float) Math.round(myPTL.getDiem() * 10) / 10));
            //region Khi mã đề không nhận diện được
            if (myPTL.getMaDe().contains("-")) {
                android.app.AlertDialog.Builder xmbox = new android.app.AlertDialog.Builder(ViewResultScore.this)
                        .setTitle("VẤN ĐỀ NHẬN DIỆN")
                        .setMessage("Hiện không nhận diện rõ mã đề, mong quý Thầy/Cô vui lòng chấm lại hoặc chấm thủ công để đảm bảo!\n\n" +
                                "MẸO: Nghiêng điện thoại từ 30-45 độ để giảm thiểu phản quang của ánh sáng.")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                                finish();
                                return;
                            }
                        });

                xmbox.create();
                xmbox.show();
            }
            //endregion
        } else {
            mytoolbar.setTitle("SBD: ------");
            AlertDialog.Builder mbox = new AlertDialog.Builder(ViewResultScore.this)
                    .setTitle("GẶP VẤN ĐỀ")
                    .setMessage("Trong quá trình chấm, máy đã không nhận diện dược một số chi tiết. Quý thầy cô vui lòng chấm lại hoặc chấm bằng tay để đảm bảo cho HS/SV")
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
        ShowScorePosition();
        //endregion

    }

    void ShowScorePosition() {
        RelativeLayout.LayoutParams my = (RelativeLayout.LayoutParams) ShowScore.getLayoutParams();
        my.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int oldTop = my.topMargin;
        int oldRight = my.rightMargin;
        my.topMargin = (int)((getWindowManager ().getDefaultDisplay().getHeight() - Math.round(Math.sqrt((resultView.getHeight()*resultView.getWidth()))))/2.4) + 5;
        if (my.topMargin < 0 || my.topMargin > getWindowManager().getDefaultDisplay().getHeight()/8)
        {
            my.topMargin = oldTop;
        }
        my.rightMargin = (int)((Math.round(Math.sqrt((resultView.getHeight()*resultView.getWidth()))) - getWindowManager().getDefaultDisplay().getWidth())) + 7;
        if (my.rightMargin < 0 || my.rightMargin > getWindowManager().getDefaultDisplay().getWidth()/5)
        {
            my.rightMargin = oldRight;
        }
        ShowScore.setLayoutParams(my);
        ShowScore.bringToFront();
    }
}
