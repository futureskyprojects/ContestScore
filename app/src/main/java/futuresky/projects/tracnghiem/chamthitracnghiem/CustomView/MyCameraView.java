package futuresky.projects.tracnghiem.chamthitracnghiem.CustomView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import org.opencv.android.JavaCameraView;

public class MyCameraView extends JavaCameraView {
    private static final String TAG = "MyCameraView";
    private AutoFocusCallback mAutoFocusTakePictureCallback = new C04451();

    class C04451 implements AutoFocusCallback {
        C04451() {
        }

        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                Log.d("tap_to_focus", "success!");
            } else {
                Log.d("tap_to_focus", "fail!");
            }
        }
    }

    public MyCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFocus() {
        if (this.mCamera != null) {
            Parameters parameters = this.mCamera.getParameters();
            parameters.setFocusMode("auto");
            this.mCamera.setParameters(parameters);
            try {
                this.mCamera.autoFocus(this.mAutoFocusTakePictureCallback);
            } catch (Exception e) {
            }
        }
    }
}
