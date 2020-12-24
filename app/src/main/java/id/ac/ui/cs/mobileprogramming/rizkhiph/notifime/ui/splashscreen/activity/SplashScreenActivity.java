package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.splashscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.main.activity.implementation.MainActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.splashscreen.view.OpenGLView;

public class SplashScreenActivity extends AppCompatActivity {
    private OpenGLView oglView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);
        oglView = findViewById(R.id.oglView);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        oglView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        oglView.onResume();
    }
}
