package com.example.breakout;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Scoreboard extends AppCompatActivity {
    Button Minus, Plus, SaveButton;
    TextView ScoreText, ScoreValue, Player1Score, Namebest1;
    int Value = 0, best1, best2, best3, best4, best5, best6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        SharedPreferences ScorePrefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
        SharedPreferences NamePrefs = getSharedPreferences("PlayerNames", MODE_PRIVATE);

        Minus = (Button) findViewById(R.id.Minus);
        Plus = (Button) findViewById(R.id.Plus);
        SaveButton = (Button) findViewById(R.id.SaveButton);
        Player1Score = (TextView) findViewById(R.id.Player1Score);
        ScoreText = (TextView) findViewById(R.id.ScoreText);
        ScoreValue = (TextView) findViewById(R.id.ScoreValue);
        Namebest1 = (TextView) findViewById(R.id.Player1);

        Player1Score.setText(Integer.toString(ScorePrefs.getInt("Score1", 0)));
        Namebest1.setText(NamePrefs.getString("Name1", "Best1"));
        //Incrémentation du score manuelle pour TESTER
        Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Value>0)Value--;
                ScoreValue.setText(Integer.toString(Value));
            }
        });
        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value++;
                ScoreValue.setText(Integer.toString(Value));
            }
        });


        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("PlayerScore", MODE_PRIVATE).edit();
                editor.putInt("Score1", Value);
                editor.apply();

            }
        });


    }


}
