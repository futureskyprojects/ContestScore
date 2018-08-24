package futuresky.projects.tracnghiem.chamthitracnghiem.Processing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import futuresky.projects.tracnghiem.chamthitracnghiem.Introduce.IntroActivity;
import futuresky.projects.tracnghiem.chamthitracnghiem.MainActivity;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class StartChecking extends AppCompatActivity {
    private final Handler mHideHandler = new Handler();
    private SharedPreferences prefs = null;
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
        setContentView(R.layout.activity_start_checking);
        prefs = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
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
        new Thread() {
            @Override
            public void run() {
                //
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (prefs.getBoolean("FirstRun", true)) {
                            prefs.edit().putBoolean("FirstRun", false).commit();
                            Intent intent = new Intent(StartChecking.this, IntroActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(StartChecking.this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
            }
        }.start();
    }


    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, 50);
    }
}
