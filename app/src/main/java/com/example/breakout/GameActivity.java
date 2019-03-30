package com.example.breakout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Hide app title bar.
        getSupportActionBar().hide();

        // Make app full screen to hide top status bar.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create the SurfaceViewThread object.
        final SurfaceViewThread surfaceViewThread = new SurfaceViewThread(getApplicationContext());

        // Get text drawing LinearLayout canvas.
        LinearLayout drawCanvas = (LinearLayout)findViewById(R.id.drawCanvas);

        // Add surfaceview object to the LinearLayout object.
        drawCanvas.addView(surfaceViewThread);
    }
}