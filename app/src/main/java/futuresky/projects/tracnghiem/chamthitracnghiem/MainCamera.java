package futuresky.projects.tracnghiem.chamthitracnghiem;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.pdf.BaseField;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import futuresky.projects.tracnghiem.chamthitracnghiem.CustomView.MyCameraView;
import futuresky.projects.tracnghiem.chamthitracnghiem.CustomView.RectPreview;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.DiemThi.DiemThi;
import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.PhieuTraLoi.PhieuTraLoi;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.DapAn.DapAnDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.Database.PhieuTraLoi.PhieuTraLoiDatabase;
import futuresky.projects.tracnghiem.chamthitracnghiem.Processing.ImageProcessing;

public class MainCamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    //region Khu vực làm full màn hình
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, 50);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        hide();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();
    }
    //endregion

    //region Khu vực khai báo
    DapAnDatabase dapAnDatabase;
    PhieuTraLoiDatabase phieuTraLoiDatabase;
    private ImageView previewX;
    public static Bitmap bitmap;
    public static Bitmap bitmap1;
    private static int halfRect = 1000;
    public static MyCameraView javaCameraView;
    public static String myPathInfo = null;
    public static String myPathResult = null;
    public static String myPathTemp = null;
    DiemThi myDiemThi;
    public static float ratio;  // Tỉ lện W/H
    BaiThi baithi;  // Lưu giữ bài thi hiện hành dược truyền vào
    Point[] arrayDst;
    Point[] arraySrc;
    Mat clone;
    Mat[] corners;
    Mat[] corners1;
    int count = 0;
    int[][][] draw;
    float[] heightRect;
    double heightResult;
    Mat hierarchy;
    public ImageProcessing imageProcessing;
    Mat mBinary;
    Mat mBinary1;
    Mat mGaussBlur;
    Mat mGray;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        public void onManagerConnected(int status) {
            switch (status) {
                case 0:
                    MainCamera.javaCameraView.enableView();
                    return;
                default:
                    super.onManagerConnected(status);
                    return;
            }
        }
    };
    float Tam;
    Mat mRga;
    Mat mRga1;
    Mat mTranform;
    private int mode;
    int myCount = 0;
    int myHeight;
    int myWidth;
    int numberArea;
    int numberMid;
    int numberSort2;
    ArrayList<Point> points;
    double[][] points1;
    double[][] points2;
    private View preview;
    Rect[] rects;
    ArrayList<MyPoint> sixPoints;
    PointF[] startPoint;
    int startY = 0;
    int startX = 0;
    ArrayList<Point> targets;
    double[][] tempPoints1;
    long time = 0;
    float[] widthRect;
    double widthResult;

    class C04182 implements OnClickListener {
        C04182() {
        }

        public void onClick(View v) {
            if (MainCamera.javaCameraView != null) {
                MainCamera.javaCameraView.setFocus();
            }
            Log.e("Saved", "Saved");
        }
    }

    class C04193 extends BroadcastReceiver {
        C04193() {
        }

        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals("finish_activity")) {
                MainCamera.this.finish();
            }
        }
    }

    class C04204 implements Runnable {
        C04204() {
        }

        public void run() {
            if (MainCamera.javaCameraView != null) {
                MainCamera.javaCameraView.setFocus();
            }
        }
    }

    class C04215 implements Runnable {
        C04215() {
        }

        public void run() {
            MainCamera.this.setFocus();
        }
    }

    class C04226 implements Runnable {
        C04226() {
        }

        public void run() {
//            Toast.makeText(MainCamera.this.getApplicationContext(), MainCamera.this.getResources().getString(C0399R.string.checkForm) + ":" + SettingActivity.form, 1).show();
        }
    }

    class C04237 implements Runnable {
        C04237() {
        }

        public void run() {
            MainCamera.javaCameraView.disableView();
        }
    }

    public class MyPoint {
        public double f97r;
        public float f98x;
        public float f99y;

        public MyPoint(float a, float b, double c) {
            this.f98x = a;
            this.f99y = b;
            this.f97r = c;
        }
    }
    //endregion

    //region Khu vực của hàm khởi tạo
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main_camera);
        this.mode = getIntent().getIntExtra("mode", -1);
        this.baithi = (BaiThi) getIntent().getSerializableExtra("BaiThi");
        if (baithi == null) {
            Toast.makeText(this, "Không tải được bài thi! Tạm ngưng việc chấm!", Toast.LENGTH_SHORT).show();
            finish();
            onBackPressed();
            return;
        }
        dapAnDatabase = new DapAnDatabase(MainCamera.this);
        phieuTraLoiDatabase = new PhieuTraLoiDatabase(MainCamera.this);
        previewX = (ImageView) findViewById(R.id.previewX);
        previewX.setVisibility(View.INVISIBLE);
        myDiemThi = new DiemThi();
        myDiemThi.findAnswer = new int[baithi.getLoaiGiay() + 20];
        myDiemThi.findKey = new int[baithi.getLoaiGiay() + 20];
        this.imageProcessing = new ImageProcessing();
        javaCameraView = (MyCameraView) findViewById(R.id.javaCameraView);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.setOnClickListener(new C04182());
        registerReceiver(new C04193(), new IntentFilter("finish_activity")); // Lỗi gì đó
    }
    //endregion

    //region Khu vực dành cho các phương thức khi tạm ngưng, tiếp tục, hay hủy bỏ activity này
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            this.mLoaderCallback.onManagerConnected(0);
        } else {
            OpenCVLoader.initAsync("3.1.0", this, this.mLoaderCallback);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }
    //endregion

    //region Khu vực cấu hình khi máy ảnh khởi động
    public void onCameraViewStarted(int width, int height) {
        runOnUiThread(new C04204());
        ratio = ((float) width) / ((float) height); // Lấy tỉ lệ màn hình
        // Phần hiển thị khung để xác định 4 ô vuông ở 4 góc tò giấy
        this.preview = new RectPreview(getApplicationContext(), null);
        this.preview.findViewById(R.id.prevView);
        this.preview.setVisibility(View.VISIBLE);

        this.myWidth = width;   // Lấy chiều rộng của màn hình thiết bị
        this.myHeight = (this.myWidth * 9) / 16;    // Chuyển thành chiều cao phù hợp chuẩn màn hình 16:9
        Tam = this.myWidth / 16;
        this.startY = (height - this.myHeight) / 2; // Tính vị trí bắt đầu của phần nhận diện ô vuông theo trục Y
        this.startX = ((width - this.myWidth) / 2); // Như trên nhưng theo X
        this.mRga1 = new Mat(height, width, CvType.CV_8UC4);    // Tạo một ma trận
        this.mRga = new Mat(this.myHeight, this.myWidth, CvType.CV_8UC4);   // Tạo ma trận thứ 2 để xử lý
        int heightCal = this.myHeight / 4;  // Chiều cao của ô nha và khoảng cách của 2 ô theo trục Oy
        int widthCal = (this.myHeight * 9) / 8; // Khoảng cách của 2 ô nhận diện theo trục Ox

        // Các tùy biến để xử lý ảnh - Khai báo mới
        this.clone = new Mat(height - heightCal, widthCal, CvType.CV_8UC4);
        this.mGray = new Mat(height - heightCal, widthCal, CvType.CV_8UC1);
        this.mBinary = new Mat(height - heightCal, widthCal, CvType.CV_8UC1);
        this.mBinary1 = new Mat(height - heightCal, widthCal, CvType.CV_8UC1);
        this.mGaussBlur = new Mat(height - heightCal, widthCal, CvType.CV_8UC1);
        this.mTranform = new Mat(height, width, CvType.CV_8UC1);
        this.hierarchy = new Mat();
        this.corners = new Mat[4];
        this.corners1 = new Mat[4];
        this.rects = new Rect[4];

        // Khoanh vùng nhận diện 4 góc được chỉ định - dùng để nhận diện góc - QUAN TRỌNG NHÉ
        this.rects[0] = new Rect(startX + (int) Tam + 30, startY + 30, heightCal, heightCal);
        this.rects[1] = new Rect(widthCal + startX + (int) Tam - 25, startY + 30, heightCal, heightCal);
        this.rects[2] = new Rect(startX + (int) Tam + 30, this.myHeight - heightCal - 10, heightCal, heightCal);
        this.rects[3] = new Rect(widthCal + startX + (int) Tam - 25, this.myHeight - heightCal - 10, heightCal, heightCal);

        // Khai báo mới các nơi chứa điểm
        this.points = new ArrayList();
        this.sixPoints = new ArrayList();

        this.numberMid = this.imageProcessing.getNumberMid(baithi.getLoaiGiay());
        this.numberArea = this.imageProcessing.getNumberArea(baithi.getLoaiGiay());
        this.numberSort2 = this.imageProcessing.getNumberSort2(baithi.getLoaiGiay());
        this.points1 = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.numberMid, 2});
        this.tempPoints1 = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.numberMid, 2});
        this.points2 = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.numberSort2, 2});
        this.targets = new ArrayList();

        // Khởi tạo nơi chúa điểm cho bài chấm
        for (int k = 0; k < baithi.getLoaiGiay() + 20; k++) {
            myDiemThi.findAnswer[k] = -1;
            myDiemThi.findKey[k] = -1;
        }
    }
    //endregion

    //region Khu vực khi mà máy ảnh đã tắt
    public void onCameraViewStopped() {
        this.mRga.release();
        this.mRga1.release();
        this.mGray.release();
        this.mBinary.release();
        this.mBinary1.release();
        this.mGaussBlur.release();
        this.hierarchy.release();
        this.clone.release();
    }
    //endregion

    //region Khu vực xử lý trên từng khung hình một
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        runOnUiThread(new C04215());
        this.mRga1 = inputFrame.rgba();
        this.mRga = this.mRga1.submat(this.startY, this.startY + this.myHeight, 0, this.myWidth);
        int heightCal = this.myHeight / 4;
        int widthCal = (this.myHeight * 9) / 8;
        float rate = ((float) this.myWidth) / 1280.0f;
        getFourSquare(rate);
        this.count = 0;
        int[] check = new int[4];
        int k = 0;
        while (k < 4) {
            ArrayList<MatOfPoint> contours = new ArrayList();
            Imgproc.findContours(this.corners1[k], contours, this.hierarchy, 1, 2, new Point(0.0d, 0.0d));
            int i = 0;
            while (i < contours.size()) {
                getCountContour(contours, i, k, rate, check);
                if (this.count == 4) {
                    if (getMidRect(widthCal, heightCal, rate)) {
                        runOnUiThread(new C04237());
                        getTrueAnswer(widthCal, heightCal);
                        intentMode(this.mode, this.arrayDst, widthCal, heightCal);
                        k = 5;
                    } else {
                        runOnUiThread(new C04226());
                        i = contours.size();
                        k = 5;
                    }
                    contours.clear();
                    k++;
                } else {
                    contours.remove(i);
                    i++;
                }
            }
            contours.clear();
            k++;
        }
        this.points.clear();
        this.sixPoints.clear();
        this.targets.clear();
        for (k = 0; k < 4; k++) {
            check[k] = 0;
        }
        return this.mRga1;
    }
    //endregion

    //region Khu vực của phương thức lấy nét
    public void setFocus() {
        if (System.currentTimeMillis() - this.time > 3000) {
            if (javaCameraView != null) {
                javaCameraView.setFocus();
            }
        }
    }
    //endregion

    //region Khu vực của phương thức lấy khớp 4 hình vuông ở 4 góc của tờ giấy
    public void getFourSquare(float rate) {
        for (int i = 0; i < 4; i++) {
            //region Vùng hiển thị khung nhận diện thật
//            Imgproc.rectangle(mRga, new Point(rects[i].x, rects[i].y),
//                    new Point(rects[i].x + rects[i].width, rects[i].y + rects[i].height),
//                    new Scalar(0, 0, 255), 2);
            //endregion
            this.corners[i] = this.mRga.submat(this.rects[i]);
            this.corners1[i] = this.corners[i].clone();
            this.corners[i].convertTo(this.corners1[i], -1, 1.0d, 100.0d);
            Imgproc.cvtColor(this.corners1[i], this.corners1[i], 6);
            Imgproc.GaussianBlur(this.corners1[i], this.corners1[i], new Size(3.0d, 3.0d), 2.0d);
            Imgproc.adaptiveThreshold(this.corners1[i], this.corners1[i], 255.0d, 0, 0, 31, (double) (5.0f * rate));
        }
    }
    //endregion

    public void getCountContour(ArrayList<MatOfPoint> contours, int i, int k, float rate, int[] check) {
        Rect rect = Imgproc.boundingRect((MatOfPoint) contours.get(i));
        int area = (int) Imgproc.contourArea((Mat) contours.get(i));
        int w = rect.width;
        int h = rect.height;
        float ratio1 = ((float) w) / ((float) h);
        double r1 = ((double) (area - Core.countNonZero(this.corners1[k].submat(rect)))) / ((double) area);
        if (r1 > this.imageProcessing.getTH(rate) && ratio1 > 0.8f && ratio1 < 1.2f) {
            Rect rect2 = new Rect(this.rects[k].x + rect.x, this.rects[k].y + rect.y, rect.width, rect.height);
            if (check[k] == 0) {
                this.count++;
                Mat mat = this.mRga;
                Point point1 = new Point((double) (this.rects[k].x + rect.x), (double) (this.rects[k].y + rect.y)); // Diểm A của HV
                Point point2 = new Point((double) ((rect.width + rect.x) + this.rects[k].x), (double) ((rect.height + rect.y) + this.rects[k].y)); // Đỉnh đối diện đỉnh A của HV
                Imgproc.rectangle(mat, point1, point2, new Scalar(255.0d, 0.0d, 0.0d), 2); // Vẽ hình chữ nhật khi nhận diện được các chấm hình vuông
                this.points.add(this.imageProcessing.getPoint(rect2));
                if (Math.min(rect2.width, rect2.height) < halfRect) {
                    halfRect = Math.min(rect2.width, rect2.height) / 2;
                }
                check[k] = 1;
            }
        }
    }

    public boolean getMidRect(int widthCal, int heightCal, float rate) {
        if (this.points.size() != 4)
            return false;
        // Sắp xếp lại các điểm trước khi tiến hành transform
        this.points = this.imageProcessing.sortCorner(this.points, this.myWidth, this.myHeight, halfRect);

        // đánh dấu các góc trước khi cắt
        this.arraySrc = new Point[]{
                (Point) this.points.get(0),
                (Point) this.points.get(1),
                (Point) this.points.get(2),
                (Point) this.points.get(3)
        };

        // Ma trận gốc trc khi cắt
        Mat matOfPoint2f = new MatOfPoint2f(this.arraySrc);

        // Liệt các điểm của ảnh mới
        this.targets.add(new Point(0.0d, 0.0d));
        this.targets.add(new Point((double) (widthCal - 1), 0.0d));
        this.targets.add(new Point(0.0d, (double) ((this.myHeight - 1) - heightCal)));
        this.targets.add(new Point((double) (widthCal - 1), (double) ((this.myHeight - 1) - heightCal)));

        // Các điểm của trận đích
        this.arrayDst = new Point[]{(Point) this.targets.get(0), (Point) this.targets.get(1), (Point) this.targets.get(2), (Point) this.targets.get(3)};
        // Ma trận đích lưu khi cắt xong
        MatOfPoint2f Destiantion = new MatOfPoint2f(this.arrayDst);
        // Kích thước để cắt
        Size size = new Size((double) widthCal, (double) (this.myHeight - heightCal));
        // ...
        Mat perspectiveMatrix = Imgproc.getPerspectiveTransform(matOfPoint2f, Destiantion);
        //
        matOfPoint2f = new Mat(widthCal, this.myHeight - heightCal, CvType.CV_8UC4);
        // Cắt xong
        Imgproc.warpPerspective(this.mRga, matOfPoint2f, perspectiveMatrix, size);
        // Lưu vào clone
        this.clone = matOfPoint2f;
        Imgproc.cvtColor(matOfPoint2f, this.mGray, 6);
        Imgproc.GaussianBlur(this.mGray, this.mGaussBlur, new Size(3.0d, 3.0d), 2.0d);
        int threadhold = this.imageProcessing.getThreadhold(rate);
        double subtract = this.imageProcessing.getSubtract(rate);
        Imgproc.adaptiveThreshold(this.mGaussBlur, this.mBinary, 255.0d, 0, 0, threadhold, subtract);
        Imgproc.adaptiveThreshold(this.mGaussBlur, this.mBinary1, 255.0d, 0, 0, threadhold, subtract);
        this.tempPoints1 = this.imageProcessing.getMidPoints(this.targets, baithi.getLoaiGiay()); // Form tạm
        double[] formArray = new double[this.numberMid];
        for (int x = 0; x < this.numberMid; x++) {
            int halfDraw = this.imageProcessing.getHalfRect(baithi.getLoaiGiay(), (float) halfRect); // Form tạm
            this.tempPoints1[x][0] = (double) ((int) this.tempPoints1[x][0]);
            this.tempPoints1[x][1] = (double) ((int) this.tempPoints1[x][1]);
            Mat mat = this.imageProcessing.getMat(this.mBinary, new Point(this.tempPoints1[x][0], this.tempPoints1[x][1]), halfDraw, halfDraw);
            Point center = this.imageProcessing.getHistogram(mat, new Point(this.tempPoints1[x][0] - ((double) halfDraw), this.tempPoints1[x][1] - ((double) halfDraw)));
            Imgproc.circle(this.clone, new Point(center.x, center.y), 2, new Scalar(255.0d, 0.0d, 0.0d), 1);  // Hiển thị mấy chấm căn chỉnh
            this.points1[x][0] = center.x;
            this.points1[x][1] = center.y;
            int area = mat.width() * mat.height();
            formArray[x] = ((double) (area - Core.countNonZero(mat))) / ((double) area);
        }
        this.points2 = this.imageProcessing.sortCorner2(this.points1, baithi.getLoaiGiay()); // Form tạm
        return true;
    }

    public void getTrueAnswer(int widthCal, int heightCal) {
        this.myCount = 0;   // Chưa thấy sử dụng
        this.draw = new int[this.numberArea][][];   // Ở đây numberArea = 4
        this.widthRect = new float[this.numberArea];
        this.heightRect = new float[this.numberArea];
        this.startPoint = new PointF[this.numberArea];
        int j = 0;
        while (j < this.numberArea) {
            int idx = 0;
            int idy = 0;
            double[] myRs = this.imageProcessing.whResult(this.points1, this.points2, j, baithi.getLoaiGiay());
            this.widthResult = myRs[0];
            this.heightResult = myRs[1];
            Rect[][] myAnswer = (Rect[][]) Array.newInstance(Rect.class, new int[]{6, 10});
            double[][] myPixel = (double[][]) Array.newInstance(Double.TYPE, new int[]{6, 10});
            this.widthRect[j] = (float) (this.widthResult / 11.0d);
            this.heightRect[j] = (float) (this.heightResult / 6.0d);
            this.startPoint[j] = this.imageProcessing.getStartPoint(j, this.points1, this.points2, baithi.getLoaiGiay());
            for (idx = 0; idx < 6; idx++) {
                for (idy = 0; idy < 10; idy++) {
                    Point point1;
                    int startX = (int) (((double) this.startPoint[j].x) + ((((double) idy) + 0.5d) * ((double) this.widthRect[j])));
                    int startY = (int) (((double) this.startPoint[j].y) + ((((double) idx) + 0.5d) * ((double) this.heightRect[j])));
                    myAnswer[idx][idy] = new Rect(startX, startY, (int) this.widthRect[j], (int) this.heightRect[j]);
                    Mat matResult = this.mBinary.submat(myAnswer[idx][idy]);
                    PointF center = new PointF((float) (matResult.width() / 2), (float) (matResult.height() / 2));
                    int radius = (int) ((this.heightRect[j] * 9.0f) / 20.0f);
                    if (this.imageProcessing.checkAreaDraw(j, baithi.getLoaiGiay(), idx) == 1) {
                        Mat mat = this.clone;
                        point1 = new Point((double) startX, (double) startY);
                        Point point2 = new Point((double) (((int) this.widthRect[j]) + startX), (double) (((float) startY) + this.heightRect[j]));
//                        Imgproc.rectangle(mat, point1, point2, new Scalar(255.0d, 255.0d, 255.0d), 2);  // Vẽ ô vuông tại mỗi số tròn

                    }
                    Mat mat2 = this.mBinary1;
                    point1 = new Point((double) (((int) center.x) + startX), (double) (((int) center.y) + startY));
                    Imgproc.circle(mat2, point1, radius, new Scalar(255.0d, 0.0d, 0.0d), (int) (this.heightRect[j] / 7.0f));    // Chấm các điểm nhận dạng
                    Mat mat3 = this.mBinary1;
                    point1 = new Point((double) (((int) center.x) + startX), (double) (((int) center.y) + startY));
                    Imgproc.circle(mat3, point1, (((int) (this.heightRect[j] / 7.0f)) + radius) + 1, new Scalar(255.0d, 0.0d, 0.0d), 2);
                    double r = this.imageProcessing.getRatePixel(this.mBinary1.submat(myAnswer[idx][idy]));
                    myPixel[idx][idy] = r;
                }
            }
            // Loại bỏ 2 trường thừa đê nhận được kết quả chính xác nhất
            if ((j != this.imageProcessing.getAreaMade(baithi.getLoaiGiay()) &&
                    j != this.imageProcessing.getAreaSBD(baithi.getLoaiGiay())))
                this.draw[j] = this.imageProcessing.findAnswer(j, myPixel, myDiemThi.findAnswer, baithi.getLoaiGiay()); // dùng để lấy đáp án đúng
//            //
//            String OK = "\n---------------------------------------\n";
//            for (int i = 0; i < idx; i++)
//            {
//                String x = "";
//                for (int k = 0;k < idy; k++)
//                {
//                    x += myPixel[i][j] + " ";
//                }
//                OK += x + "\n";
//            }
//            OK+="\n---------------------------------------\n";
//            Log.d("myPixel " + j, OK);
//            //
            if (j == this.imageProcessing.getAreaMade(baithi.getLoaiGiay()) ||
                    j == this.imageProcessing.getAreaSBD(baithi.getLoaiGiay())) {
                if (j == this.imageProcessing.getAreaMade(baithi.getLoaiGiay())) {
//                    Log.d("J="+j, "Của mã đề");
                    myDiemThi.made = this.imageProcessing.getMade(myPixel);
                    int[] md = this.imageProcessing.convertStringToArray(myDiemThi.made);
                    for (idx = 2; idx < 5; idx++) {
                        if (md[4 - idx] > -1) {
                            startX = (int) (((double) this.startPoint[j].x) + ((((double) md[4 - idx]) + 0.5d) * ((double) this.widthRect[j])));
                            startY = (int) (((double) this.startPoint[j].y) + ((((double) idx) + 0.5d) * ((double) this.heightRect[j])));
                            int radius = (int) ((this.heightRect[j] * 9.0f) / 20.0f);
                            Mat mat = this.clone;
                            Point point = new Point((double) (((float) startX) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)), (double) (((float) startY) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)));
                            Imgproc.circle(mat, point, radius, new Scalar(0.0d, 255.0d, 0.0d), (int) (this.heightRect[j] / 7.0f));
                        }
                    }
                } else if (j == this.imageProcessing.getAreaSBD(baithi.getLoaiGiay())) {
//                    Log.d("J="+j, "Của số báo danh");
                    myDiemThi.sobaodanh = this.imageProcessing.getSoBaoDanh(myPixel);
                    int[] sbd = this.imageProcessing.convertStringToArray(myDiemThi.sobaodanh);
                    for (idx = 0; idx < 6; idx++) {
                        if (sbd[5 - idx] > -1) {
                            startX = (int) (((double) this.startPoint[j].x) + ((((double) sbd[5 - idx]) + 0.5d) * ((double) this.widthRect[j])));
                            startY = (int) (((double) this.startPoint[j].y) + ((((double) idx) + 0.5d) * ((double) this.heightRect[j])));
                            int radius = (int) ((this.heightRect[j] * 9.0f) / 20.0f);
                            Mat mat2 = this.clone;
                            Point point = new Point((double) (((float) startX) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)), (double) (((float) startY) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)));
                            Imgproc.circle(mat2, point, radius, new Scalar(0.0d, 255.0d, 0.0d), (int) (this.heightRect[j] / 7.0f));
                        }
                    }
                }
            } else if (this.mode == 0) {
                for (idx = 0; idx < 5; idx++) {
                    for (idy = 0; idy < 10; idy++) {
                        startX = (int) (((double) this.startPoint[j].x) + ((((double) idy) + 0.5d) * ((double) this.widthRect[j])));
                        startY = (int) (((double) this.startPoint[j].y) + ((((double) idx) + 0.5d) * ((double) this.heightRect[j])));
                        if (this.draw[j][idx][idy] == 1) {
                            int radius = (int) ((this.heightRect[j] * 9.0f) / 20.0f);
                            Mat mat3 = this.clone;
                            Point point = new Point((double) (((float) startX) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)), (double) (((float) startY) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)));
                            Imgproc.circle(mat3, point, radius, new Scalar(0.0d, 255.0d, 0.0d, 1.0d), (int) (this.heightRect[j] / 7.0f));
                        }
                    }
                }
            }
            j++;
        }
//        ShowMat(clone);
//        Log.d("THÔNG TIN", "MÃ ĐỀ: " + myDiemThi.made + "\n" +
//                "SỐ BÁO DANH: " + myDiemThi.sobaodanh);
//        String tam = "";
//        for (int vc = 0; vc < myDiemThi.findAnswer.length; vc++)
//            tam += myDiemThi.findAnswer[vc] + " ";
//        Log.d("TRẢ LỜI", "\n---------------------------------\n" +
//                tam +
//                "\n---------------------------------\n");
//        bitmap1 = Bitmap.createBitmap(widthCal, this.myHeight - heightCal, Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(this.mBinary1, bitmap1);
//        saveImage(bitmap1, "answer/binary", "binaryImage1", 0);
    }

    public void intentMode(int mode, Point[] arrayDst, int widthCal, int heightCal) {
        bitmap = Bitmap.createBitmap(widthCal, this.myHeight - heightCal, Bitmap.Config.ARGB_8888);
        int id;
        int checkId;
        Intent intent;
        if (mode == 0) {    // Dành cho lúc nhập đáp án
//            Utils.matToBitmap(this.clone, bitmap);
//            Bitmap tempBitmap = Bitmap.createBitmap(widthCal, this.myHeight - heightCal, Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(this.clone, tempBitmap);
////            saveImage(tempBitmap, "key/key", "keyImage_temp", 2);
//            id = dapAnDatabase.getMaxID() + 1;
//            checkId = dapAnDatabase.checkMade(imageProcessing.convertMadeToText(myDiemThi.made), Integer.parseInt(baithi.getId()));
////            if (checkId > -1) {
////                saveImage(bitmap, "key/key", "keyImage" + checkId, 0);
////            } else {
////                saveImage(bitmap, "key/key", "keyImage" + id, 0);
////            }
//            long startIntent = System.currentTimeMillis();
//            intent = new Intent(this, ViewEditKey.class);
//            intent.putExtra("myPath", myPathResult);
//            intent.putExtra("myPathTemp", myPathTemp);
//            intent.putExtra("made", myDiemThi.made);
//            String key = this.imageProcessing.convertArrayToString(myDiemThi.findAnswer);
//            String textKey = this.imageProcessing.convertMadeToText(myDiemThi.made);
//            intent.putExtra("key", key);
//            intent.putExtra("textKey", textKey);
//            startActivity(intent);
//            Log.d("vinhtuanleTimeStIntent", (System.currentTimeMillis() - startIntent) + " ");
        } else if (mode == 1) {
            id = phieuTraLoiDatabase.MaxID() + 1;
            if (dapAnDatabase != null) {
                checkId = dapAnDatabase.LayIdMaDe(Integer.parseInt(imageProcessing.convertMadeToText(myDiemThi.made)),
                        Integer.parseInt(baithi.getId()));  // Lấy id của mã đè hiện tại trong csdl
                if (checkId > -1) {     // nếu tồn tại, tức checkId là mã số của mã đề đó thì
//                    myDiemThi.findKey = dapAnDatabase.layDapAn(checkId).ArrayDapAn();
                    myDiemThi.findKey = this.imageProcessing.convertKeyToArray(dapAnDatabase.layDapAn(checkId).getDapAn(),
                            baithi.getLoaiGiay(), baithi.getSoCau());   // Chuyển đổi chuỗi đáp án sang dãy các đáp án tương ứng loại giấy và số câu
                    String tam = "";
                    for (int vc = 0; vc < myDiemThi.findKey.length; vc++)
                        tam += myDiemThi.findKey[vc] + " ";
                    Log.d("ĐÁP ÁN", "\n---------------------------------\n" +
                            tam +
                            "\n---------------------------------\n");
                }
            }
            String tam = "";
            for (int vc = 0; vc < myDiemThi.findAnswer.length; vc++)
                tam += myDiemThi.findAnswer[vc] + " ";
            Log.d("TRẢ LỜI", "\n---------------------------------\n" +
                    tam +
                    "\n---------------------------------\n");
//            this.imageProcessing.printAnswer(1, myDiemThi.findAnswer);
            int[] trueAnswer = this.imageProcessing.getTrueAnswer(myDiemThi.findAnswer, myDiemThi.findKey, baithi.getLoaiGiay());
            Point[] rectTrue = this.imageProcessing.getRectTrue(this.points1, this.points2, this.widthResult, baithi.getLoaiGiay());
            for (int m = 20; m < baithi.getSoCau() + 20; m++) {
                Log.d("trueAnswer", trueAnswer[m] + " ");
                if (trueAnswer[m] == 1) {
                    Imgproc.circle(this.clone, rectTrue[m], 2, new Scalar(0.0d, 255.0d, 0.0d, 1.0d), 3);
                } else {
                    Imgproc.circle(this.clone, rectTrue[m], 2, new Scalar(255.0d, 0.0d, 0.0d, 1.0d), 3);
                }
            }
            int j = 0;
            while (j < this.numberArea) {
                if (!(j == this.imageProcessing.getAreaMade(baithi.getLoaiGiay()) || j == this.imageProcessing.getAreaSBD(baithi.getLoaiGiay()) || mode != 1)) {
                    for (int idx = 0; idx < 5; idx++) {
                        for (int idy = 0; idy < 10; idy++) {
                            int i;
                            int startX = (int) (((double) this.startPoint[j].x) + ((((double) idy) + 0.5d) * ((double) this.widthRect[j])));
                            int startY = (int) (((double) this.startPoint[j].y) + ((((double) idx) + 0.5d) * ((double) this.heightRect[j])));
                            int radius = (int) ((this.heightRect[j] * 9.0f) / 20.0f);
                            if (this.draw[j][idx][idy] == 1) {
                                if (myDiemThi.findKey[this.imageProcessing.findTrueAnswer(j, idy, baithi.getLoaiGiay())] == /*4*/5 - idx) {
                                    i = radius;
                                    Imgproc.circle(this.clone, new Point((double) (((float) startX) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)), (double) (((float) startY) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM))), i, new Scalar(0.0d, 255.0d, 0.0d, 1.0d), (int) (this.heightRect[j] / 7.0f));
                                }
                            }
                            if (this.draw[j][idx][idy] == 1) {
                                i = radius;
                                Imgproc.circle(this.clone, new Point((double) (((float) startX) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)), (double) (((float) startY) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM))), i, new Scalar(255.0d, 0.0d, 0.0d, 1.0d), (int) (this.heightRect[j] / 7.0f));
                            } else {
                                if (myDiemThi.findKey[this.imageProcessing.findTrueAnswer(j, idy, baithi.getLoaiGiay())] == 5/*4*/ - idx) {
                                    i = radius;
                                    Imgproc.circle(this.clone, new Point((double) (((float) startX) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM)), (double) (((float) startY) + (this.widthRect[j] / BaseField.BORDER_WIDTH_MEDIUM))), i, new Scalar(249.0d, 244.0d, 0.0d, 1.0d), (int) (this.heightRect[j] / 7.0f));
                                }
                            }
                        }
                    }
                }
                j++;
            }
            Utils.matToBitmap(this.clone, bitmap);
////            bitmap = saveImage(bitmap, "answer/answer", "answerImage" + id, 0);
//            intent = new Intent(this, ViewAnswer.class);
//            intent.putExtra("myPath", myPathResult);
//            myDiemThi.score = this.imageProcessing.findScore(myDiemThi.findAnswer, myDiemThi.findKey, baithi.getSoCau());
//            String text = myDiemThi.score + "";
////            saveImage(this.imageProcessing.cropInfo(arrayDst, bitmap), "answer/info", "infoImage" + id, 1);
//            float diem = ((float) Math.round(100.0f * (((float) myDiemThi.score) * this.imageProcessing.getValueSentence(
//                    baithi.getSoCau(), 10)))) / 100.0f;
//            String textScore = this.imageProcessing.getTextScore(baithi.getSoCau(), text, diem);
//            Log.d("vinhtuanleScore", textScore);
//            intent.putExtra("textScore", textScore);
//            String textMade = this.imageProcessing.convertMadeToText(MyScore.made);
//            String textSobaodanh = this.imageProcessing.convertMadeToText(MyScore.sobaodanh);
//            String textAnswer = this.imageProcessing.convertArrayToString(MyScore.findAnswer);
//            intent.putExtra("textMade", textMade);
//            ActivityManager.answerDatabase.addAnswer(id, diem, MyScore.sobaodanh, textSobaodanh, MyScore.made, textMade, textAnswer, textScore, myPathResult, myPathInfo, HomeActivity.contestID);
//            startActivity(intent);
        }
    }

    void ShowText(final String myText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainCamera.this, myText, Toast.LENGTH_LONG).show();
            }
        });
    }

    void ShowMat(Mat des) {
        final Bitmap mybm = Bitmap.createBitmap(des.width(), des.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(des, mybm);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewX.setVisibility(View.VISIBLE);
                previewX.bringToFront();
                previewX.setImageBitmap(mybm);
                javaCameraView.setVisibility(View.INVISIBLE);
                javaCameraView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
