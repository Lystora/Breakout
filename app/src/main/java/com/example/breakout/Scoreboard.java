package com.example.breakout;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard extends AppCompatActivity {
    Button Minus, Plus, RefreshButton, SAVEALL;
    TextView ScoreText, ScoreBoard;
    EditText PlayerName;
    List<String> PlayerList;
    int Value = 0;
    SharedPreferences prefs, prefsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        PlayerList = new ArrayList<String>();
        RecupList();
        //Config des Widgets
        Minus = (Button) findViewById(R.id.activity_score_minus);
        Plus = (Button) findViewById(R.id.activity_score_plus);
        RefreshButton = (Button) findViewById(R.id.activity_score_refresh);
        SAVEALL = (Button) findViewById(R.id.activity_score_saveall);
        ScoreText = (TextView) findViewById(R.id.activity_score_score);
        ScoreBoard = (TextView) findViewById(R.id.activity_score_scoreboard);
        PlayerName = (EditText) findViewById(R.id.activity_score_playername);
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
        RefreshButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(PlayerName.getText().toString(), Value);
                editor.apply();

                if(!PlayerName.getText().toString().isEmpty()){
                    if(PlayerList.size()==5){
                        addPlayer(PlayerName.getText().toString());
                        SortList();
                        removePlayer();
                    }else{
                        addPlayer(PlayerName.getText().toString());
                        SortList();
                    }
                }
                DisplayScore();
            }
        });
        SAVEALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveList();
            }
        });

    }
    public void addPlayer(String n){
        PlayerList.add(n);
    }
    public void SortList(){
        prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
        boolean sorted = false;
        int taille = PlayerList.size();
        while(!sorted){
            sorted=true;
            for(int i=0; i<taille-1; i++){
                int val1=prefs.getInt(PlayerList.get(i),0);
                int val2=prefs.getInt(PlayerList.get(i+1),0);
                if(val1< val2){
                    String temp = PlayerList.get(i);
                    PlayerList.set(i, PlayerList.get(i+1));
                    PlayerList.set(i+1, temp);
                    sorted = false;
                }
            }
            taille--;
        }
    }
    public void removePlayer(){
        PlayerList.remove(PlayerList.size()-1);
    }
    public void DisplayScore(){
        prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
        String chaine = "SCOREBOARD : " +"\n";
        String c;
        ScoreBoard.setText("SCOREBOARD : ");
        for(int i=0; i<PlayerList.size(); i++){
            if(PlayerList.get(i).isEmpty()){
                c = "---"+"\n";
            }else{
                c = PlayerList.get(i)+ "         "+prefs.getInt(PlayerList.get(i), 0)+"\n";
            }
            chaine += c;
        }
        ScoreBoard.setText(chaine);
    }
    public void SaveList(){
        prefsPlayer = getSharedPreferences("PlayerNames", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsPlayer.edit();
        for(int i=0; i<PlayerList.size(); i++){
            String key ="best"+Integer.toString(i+1);
            editor.putString(key, PlayerList.get(i));
            editor.apply();
        }
    }
    public void RecupList(){
        prefsPlayer = getSharedPreferences("PlayerNames", MODE_PRIVATE);
        for(int i=0; i<5; i++){
            String key = "best"+Integer.toString(i+1);
            String Player = prefsPlayer.getString(key, "no player");
            //if(!Player.isEmpty()) {
                PlayerList.add(Player);
            //}
        }
    }
}
