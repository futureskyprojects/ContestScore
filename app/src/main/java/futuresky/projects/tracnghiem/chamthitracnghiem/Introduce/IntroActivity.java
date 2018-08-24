package futuresky.projects.tracnghiem.chamthitracnghiem.Introduce;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class IntroActivity extends AppCompatActivity {

    private ViewPager mIntroView;
    Timer timer;
    int currentPage = 0;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        mIntroView = (ViewPager) findViewById(R.id.introView);

        mIntroView.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        mIntroView.setPageTransformer(false, new IntroPageTranfomer());
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (mIntroView.getCurrentItem() == mIntroView.getAdapter().getCount()-1) {
                    // Kết thúc phần giới thiệu
                }
                mIntroView.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 3000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hide();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();
    }
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, 50);
    }

}
