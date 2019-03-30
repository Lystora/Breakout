package com.example.breakout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btPlay, btScore;
    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Title = (TextView) findViewById(R.id.activity_main_title);
        btPlay = (Button) findViewById(R.id.activity_main_bt_play);
        btScore = (Button) findViewById(R.id.activity_main_bt_score);

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
                Intent ScoreActivity = new Intent(MainActivity.this, Scoreboard.class);
                startActivity(ScoreActivity);

            }
        });

    }
}
