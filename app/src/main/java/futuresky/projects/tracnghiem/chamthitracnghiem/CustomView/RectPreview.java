package futuresky.projects.tracnghiem.chamthitracnghiem.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import futuresky.projects.tracnghiem.chamthitracnghiem.MainCamera;

public class RectPreview extends View {
    public static int deviceHeight;
    public static int deviceWidth;
    public static float ratio;

    public RectPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        deviceWidth = getResolution().x;
        deviceHeight = getResolution().y;
        ratio = ((float) deviceWidth) / ((float) deviceHeight);
        if (ratio < MainCamera.ratio) {
            deviceHeight = (deviceWidth * 9) / 16;
        } else {
                deviceWidth = (int) (((float) deviceHeight) * MainCamera.ratio);
            deviceHeight = (deviceWidth * 9) / 16;
        }
        Log.e("Độ phân giải", deviceWidth + "x" + deviceHeight);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(1);
        paint.setStyle(Style.STROKE);
        paint.setAlpha(150);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5.0f);
        int startY = (getResolution().y - deviceHeight) / 2;
        int startX = (getResolution().x - deviceWidth) / 2 + (int)(deviceWidth/2 - deviceHeight/this.ratio) - getResolution().x/16;
        canvas.drawRect(new Rect(startX, startY, (deviceHeight / 4) + startX, (deviceHeight / 4) + startY), paint);
        canvas.drawRect(new Rect(((deviceHeight * 9) / 8) + startX, startY, (((deviceHeight * 9) / 8) + startX) + (deviceHeight / 4), (deviceHeight / 4) + startY), paint);
        canvas.drawRect(new Rect(startX, (deviceHeight + startY) - (deviceHeight / 4), (deviceHeight / 4) + startX, deviceHeight + startY), paint);
        canvas.drawRect(new Rect(((deviceHeight * 9) / 8) + startX, (deviceHeight + startY) - (deviceHeight / 4), (((deviceHeight * 9) / 8) + startX) + (deviceHeight / 4), deviceHeight + startY), paint);
    }

    public Point getResolution() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new Point(size.x, size.y);
    }
}
