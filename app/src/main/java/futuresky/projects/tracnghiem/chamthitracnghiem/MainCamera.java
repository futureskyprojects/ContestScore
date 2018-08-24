package futuresky.projects.tracnghiem.chamthitracnghiem;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import futuresky.projects.tracnghiem.chamthitracnghiem.CustomView.MyCameraView;
import futuresky.projects.tracnghiem.chamthitracnghiem.CustomView.RectPreview;
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
    public static Bitmap bitmap;
    public static Bitmap bitmap1;
    private static int halfRect = 1000;
    public static MyCameraView javaCameraView;
    public static String myPathInfo = null;
    public static String myPathResult = null;
    public static String myPathTemp = null;
    public static float ratio;  // Tỉ lện W/H
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
//        Tam = ((this.myWidth / 2) - (this.myHeight / this.ratio));
        Tam = this.myWidth/16;
//        Log.d("TẠM", "Tọa độ tạm là: " + Tam);
        this.startY = (height - this.myHeight) / 2; // Tính vị trí bắt đầu của phần nhận diện ô vuông theo trục Y
        this.startX = ((width - this.myWidth) / 2); // Như trên nhưng theo X
        this.mRga1 = new Mat(height, width, CvType.CV_8UC4);    // Tạo một ma trận
        this.mRga = new Mat(this.myHeight, this.myWidth, CvType.CV_8UC4);   // Tạo ma trận thứ 2 để xử lý
        int heightCal = this.myHeight / 4;  // Chiều cao của ô nha và khoảng cách của 2 ô theo trục Oy
        int widthCal = (this.myHeight * 9) / 8; // Khoảng cách của 2 ô nhận diện theo trục Ox
        Log.i("Cal", "W: " + widthCal + "\nH: " + heightCal);

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

        // Khoanh vùng nhận diện 4 góc được chỉ định
        this.rects[0] = new Rect(startX + (int)Tam, startY, heightCal, heightCal);
        this.rects[1] = new Rect(widthCal + startX + (int)Tam, startY, heightCal, heightCal);
        this.rects[2] = new Rect(startX + (int)Tam, this.myHeight - heightCal, heightCal, heightCal);
        this.rects[3] = new Rect(widthCal + startX  + (int)Tam, this.myHeight - heightCal, heightCal, heightCal);

        // Khai báo mới các nơi chứa điểm
        this.points = new ArrayList();
        this.sixPoints = new ArrayList();
        Log.d("Số câu", 40 + " ");
        Log.d("Loại phiếu", 40 + " ");

        this.numberMid = this.imageProcessing.getNumberMid(40);
        this.numberArea = this.imageProcessing.getNumberArea(40);
        this.numberSort2 = this.imageProcessing.getNumberSort2(40);
        this.points1 = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.numberMid, 2});
        this.tempPoints1 = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.numberMid, 2});
        this.points2 = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.numberSort2, 2});
        this.targets = new ArrayList();

        // Khởi tạo nơi chúa điểm cho bài chấm
//        for (int k = 0; k < 40 + 20; k++) {
//            if (!(MyScore.findAnswer == null || MyScore.findKey == null)) {
//                MyScore.findAnswer[k] = -1;
//                MyScore.findKey[k] = -1;
//            }
//        }
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
        Log.d("Tọa độ cắt", "Hàng: " + this.startY + " đến " + (this.myHeight + startY) + " là " + myHeight +
                "\nCột: " + this.startX + " đến " + (this.myWidth) + " là " + myWidth);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainCamera.this, "Nhận diện xong mẫu giấy!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
//                    long startTime = System.currentTimeMillis();
//                    if (getMidRect(widthCal, heightCal, rate)) {
//                        Log.d("vinhtuanleTimeMidRect", (System.currentTimeMillis() - startTime) + "");
//                        runOnUiThread(new C04237());
//                        long trueTime = System.currentTimeMillis();
////                        getTrueAnswer(widthCal, heightCal);
//                        Log.d("vinhtuanleTimeTrue", (System.currentTimeMillis() - trueTime) + "");
//                        long intentTime = System.currentTimeMillis();
////                        intentMode(this.mode, this.arrayDst, widthCal, heightCal);
//                        Log.d("vinhtuanleTimeIntent", (System.currentTimeMillis() - intentTime) + " ");
//                        Log.d("vinhtuanleTimeTotal", (System.currentTimeMillis() - startTime) + " ");
//                        k = 5;
//                    } else {
//                        runOnUiThread(new C04226());
//                        i = contours.size();
//                        k = 5;
//                    }
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
            this.time = System.currentTimeMillis();
        }
    }
    //endregion

    //region Khu vực của phương thức lấy khớp 4 hình vuông ở 4 góc của tờ giấy
    public void getFourSquare(float rate) {
        for (int i = 0; i < 4; i++) {
//            Log.d("THỬ NGHIỆM", "Tọa độ: " + rects[i].x + " " + rects[i].y);
            Imgproc.rectangle(mRga, new Point(rects[i].x,rects[i].y),
                    new Point(rects[i].x + rects[i].width, rects[i].y + rects[i].height),
                    new Scalar(0,0,255), 2);
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
//        Log.e("Hình chữ nhật thứ " + k + " ", rect.x + " " + rect.y + " " + rect.width + " " + rect.height);
        int area = (int) Imgproc.contourArea((Mat) contours.get(i));
        int w = rect.width;
        int h = rect.height;
        float ratio1 = ((float) w) / ((float) h);
        double r1 = ((double) (area - Core.countNonZero(this.corners1[k].submat(rect)))) / ((double) area);
        if (r1 > this.imageProcessing.getTH(rate) && ratio1 > 0.8f && ratio1 < 1.2f) {
            Rect rect2 = new Rect(this.rects[k].x + rect.x, this.rects[k].y + rect.y, rect.width, rect.height);
            if (check[k] == 0) {
                this.count++;
                Mat mat = this.mRga; // Đặt
                Point point1 = new Point((double) (this.rects[k].x + rect.x), (double) (this.rects[k].y + rect.y)); // Diểm A của HV
                Point point2 = new Point((double) ((rect.width + rect.x) + this.rects[k].x), (double) ((rect.height + rect.y) + this.rects[k].y)); // Đỉnh đối diện đỉnh A của HV
                Imgproc.rectangle(mat, point1, point2, new Scalar(255.0d, 0.0d, 0.0d), 2); // Vẽ hình chữ nhật khi nhận diện được các chấm hình vuông
                this.points.add(this.imageProcessing.getPoint(rect2));
                if (Math.min(rect2.width, rect2.height) < halfRect) {
                    halfRect = Math.min(rect2.width, rect2.height) / 2;
                }
//                Log.d("Đường viền hình vuông " + k, w + " " + h + " r: " + r1 + " tỉ lệ: " + ratio1 + " area:" + area);
                check[k] = 1;
            }
//            Imgproc.circle(mRga, new Point(myWidth/2, myHeight/2), 10, new Scalar(0,255,0), 10);
//            Log.d("Bỏ qua đường viền", "OK!");
        }
    }

//    public boolean getMidRect(int widthCal, int heightCal, float rate) {
//        if (this.points.size() != 4) {
//            return false;
//        }
//        this.points = this.imageProcessing.sortCorner(this.points, this.myWidth, this.myHeight, halfRect);
//        for (int v = 0; v < 4; v++) {
//            Log.e("sort" + v, ((Point) this.points.get(v)).x + " " + ((Point) this.points.get(v)).y);
//        }
//        Log.e("sortCount", this.points.size() + " ");
//        Log.e("success", "successful");
//        this.arraySrc = new Point[]{(Point) this.points.get(0), (Point) this.points.get(1), (Point) this.points.get(2), (Point) this.points.get(3)};
//        Mat matOfPoint2f = new MatOfPoint2f(this.arraySrc);
//        this.targets.add(new Point(0.0d, 0.0d));
//        this.targets.add(new Point((double) (widthCal - 1), 0.0d));
//        this.targets.add(new Point(0.0d, (double) ((this.myHeight - 1) - heightCal)));
//        this.targets.add(new Point((double) (widthCal - 1), (double) ((this.myHeight - 1) - heightCal)));
//        this.arrayDst = new Point[]{(Point) this.targets.get(0), (Point) this.targets.get(1), (Point) this.targets.get(2), (Point) this.targets.get(3)};
//        matOfPoint2f = new MatOfPoint2f(this.arrayDst);
//        Size size = new Size((double) widthCal, (double) (this.myHeight - heightCal));
//        Mat perspectiveMatrix = Imgproc.getPerspectiveTransform(matOfPoint2f, matOfPoint2f);
//        matOfPoint2f = new Mat(widthCal, this.myHeight - heightCal, CvType.CV_8UC4);
//        long timeCV = System.currentTimeMillis();
//        long perTime = System.currentTimeMillis();
//        Imgproc.warpPerspective(this.mRga, matOfPoint2f, perspectiveMatrix, size);
//        Log.d("vinhtuanleTimePer", (System.currentTimeMillis() - perTime) + " ");
//        long perTime1 = System.currentTimeMillis();
//        this.clone = matOfPoint2f;
//        Log.d("vinhtuanleTimeGan", (System.currentTimeMillis() - perTime1) + " ");
//        long colorTime = System.currentTimeMillis();
//        Imgproc.cvtColor(matOfPoint2f, this.mGray, 6);
//        Log.d("vinhtuanleTimeColor", (System.currentTimeMillis() - colorTime) + " ");
//        long gaussTime = System.currentTimeMillis();
//        Imgproc.GaussianBlur(this.mGray, this.mGaussBlur, new Size(3.0d, 3.0d), 2.0d);
//        Log.d("vinhtuanleTimeGauss", (System.currentTimeMillis() - gaussTime) + " ");
//        int threadhold = this.imageProcessing.getThreadhold(rate);
//        double subtract = this.imageProcessing.getSubtract(rate);
//        long timeThread = System.currentTimeMillis();
//        Imgproc.adaptiveThreshold(this.mGaussBlur, this.mBinary, 255.0d, 0, 0, threadhold, subtract);
//        Imgproc.adaptiveThreshold(this.mGaussBlur, this.mBinary1, 255.0d, 0, 0, threadhold, subtract);
//        Log.d("vinhtuanleTimeTH", (System.currentTimeMillis() - timeThread) + " ");
//        Log.d("vinhtuanleTimeCV", (System.currentTimeMillis() - timeCV) + " ");
//        Log.e("success", "tranformed row" + this.mBinary.rows() + "col" + this.mBinary.cols());
//        this.tempPoints1 = this.imageProcessing.getMidPoints(this.targets, 40); // Form tạm
//        Log.d("vinhtuanleCheck", this.numberArea + " " + this.numberMid + " " + this.numberSort2);
//        double[] formArray = new double[this.numberMid];
//        for (int x = 0; x < this.numberMid; x++) {
//            int halfDraw = this.imageProcessing.getHalfRect(40, (float) halfRect); // Form tạm
//            Log.d("vinhtuanleHalfDraw", halfDraw + " " + halfRect);
//            this.tempPoints1[x][0] = (double) ((int) this.tempPoints1[x][0]);
//            this.tempPoints1[x][1] = (double) ((int) this.tempPoints1[x][1]);
//            Log.d("getPoint", this.tempPoints1[x][0] + " " + this.tempPoints1[x][1]);
//            Log.e("historyHalfRect" + x, halfDraw + " " + (this.tempPoints1[x][0] - ((double) halfDraw)) + " " + (this.tempPoints1[x][1] - ((double) halfDraw)));
//            Mat mat = this.imageProcessing.getMat(this.mBinary, new Point(this.tempPoints1[x][0], this.tempPoints1[x][1]), halfDraw, halfDraw);
//            long timeHistory = System.currentTimeMillis();
//            Point center = this.imageProcessing.getHistogram(mat, new Point(this.tempPoints1[x][0] - ((double) halfDraw), this.tempPoints1[x][1] - ((double) halfDraw)));
//            Log.d("vinhtuanleTimeHistory", (System.currentTimeMillis() - timeHistory) + " ");
//            long timeDraw = System.currentTimeMillis();
//            Imgproc.circle(this.clone, new Point(center.x, center.y), 2, new Scalar(255.0d, 0.0d, 0.0d, 1.0d), 1+3);
//            Log.d("vinhtuanleTimeCircle", (System.currentTimeMillis() - timeDraw) + " ");
//            this.points1[x][0] = center.x;
//            this.points1[x][1] = center.y;
//            int area = mat.width() * mat.height();
//            formArray[x] = ((double) (area - Core.countNonZero(mat))) / ((double) area);
//        }
//        Log.d("vinhtuanleTimeCheckForm", (System.currentTimeMillis() - System.currentTimeMillis()) + " ");
//        this.points2 = this.imageProcessing.sortCorner2(this.points1, 40); // Form tạm
//        return true;
//    }
}
