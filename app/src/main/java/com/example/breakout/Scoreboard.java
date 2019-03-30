package com.example.breakout;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Scoreboard extends AppCompatActivity {
    Button Minus, Plus, SaveButton;
    TextView ScoreText, N_best1, N_best2, N_best3, N_best4, N_best5;
    int Value = 0, lastScore, best1, best2, best3, best4, best5;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //Config des Widgets
        Minus = (Button) findViewById(R.id.activity_score_minus);
        Plus = (Button) findViewById(R.id.activity_score_plus);
        SaveButton = (Button) findViewById(R.id.activity_score_save);
        ScoreText = (TextView) findViewById(R.id.activity_score_score);
        N_best1 = (TextView) findViewById(R.id.activity_score_best1);
        N_best2 = (TextView) findViewById(R.id.activity_score_best2);
        N_best3 = (TextView) findViewById(R.id.activity_score_best3);
        N_best4 = (TextView) findViewById(R.id.activity_score_best4);
        N_best5 = (TextView) findViewById(R.id.activity_score_best5);



        //Chargement des anciens scores
        RefreshScores();

        //Incrémentation du score manuelle pour TESTER
        Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Value>0)Value--;
                ScoreText.setText("Score : " + Value);
            }
        });
        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value++;
                ScoreText.setText("Score : " + Value);
            }
        });

        //Sauvegarde du score actuel
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshScores();
            }
        });

    }
    public void RefreshScores(){
        prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        lastScore = prefs.getInt("lastScore", 0);
        best1 = prefs.getInt("best1", 0);
        best2 = prefs.getInt("best2", 0);
        best3 = prefs.getInt("best3", 0);
        best4 = prefs.getInt("best4", 0);
        best5 = prefs.getInt("best5", 0);
        if(lastScore > best5){
                best5 = lastScore;
                editor.putInt("best5", best5);
                editor.apply();
        }
        if(lastScore > best4){
            int temp = best4;
            best4 = lastScore;
            best5 = temp;
            editor.putInt("best4", best4);
            editor.putInt("best5", best5);
            editor.apply();
        }
        if(lastScore > best3){
            int temp = best3;
            best3 = lastScore;
            best4 = temp;
            editor.putInt("best3", best3);
            editor.putInt("best4", best4);
            editor.apply();
        }
        if(lastScore > best2){
            int temp = best2;
            best2 = lastScore;
            best3 = temp;
            editor.putInt("best2", best2);
            editor.putInt("best3", best3);
            editor.apply();
        }
        if(lastScore > best1){
            int temp = best1;
            best1 = lastScore;
            best2 = temp;
            editor.putInt("best1", best1);
            editor.putInt("best2", best2);
            editor.apply();
        }
    }

}
