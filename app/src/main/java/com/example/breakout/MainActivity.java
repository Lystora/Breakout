package com.example.breakout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btPlay, btScore;
    LinearLayout background;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide app title bar.
        getSupportActionBar().hide();

        // Make app full screen to hide top status bar.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        title = (TextView) findViewById(R.id.activity_main_title);
        background = (LinearLayout) findViewById(R.id.activity_main);
        btPlay = (Button) findViewById(R.id.activity_main_bt_play);
        btScore = (Button) findViewById(R.id.activity_main_bt_score);

        // Couleurs
        background.setBackgroundColor(Color.BLACK);
        title.setTextColor(Color.WHITE);
        btPlay.setBackgroundColor(Color.WHITE);
        btScore.setBackgroundColor(Color.WHITE);

        // Action sur les boutons
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GameActivity = new Intent(MainActivity.this, GameActivity.class);
                startActivity(GameActivity);
            }
        });
        btScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean show;
                show = false;
                Intent ScoreActivity = new Intent(MainActivity.this, Scoreboard.class);
                ScoreActivity.putExtra("ShowButton", show);
                startActivity(ScoreActivity);

            }
        });

    }
}
