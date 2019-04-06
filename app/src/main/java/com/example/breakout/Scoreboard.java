package com.example.breakout;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Scoreboard extends AppCompatActivity {
    Button AddScoreButton, SAVEALL;
    TextView ScoreBoard;
    EditText PlayerName;
    List<String> PlayerList;
    String currentDate;
    SharedPreferences prefs, prefsPlayer, prefsDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        PlayerList = new ArrayList<String>();
        prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
        prefsPlayer = getSharedPreferences("PlayerNames", MODE_PRIVATE);
        RecupList();
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        //Config des Widgets
        AddScoreButton = (Button) findViewById(R.id.activity_score_addScore);
        SAVEALL = (Button) findViewById(R.id.activity_score_saveall);
        ScoreBoard = (TextView) findViewById(R.id.activity_score_scoreboard);
        PlayerName = (EditText) findViewById(R.id.activity_score_playername);
        //Sauvegarde du score actuel
        AddScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
                prefsDate = getSharedPreferences("ScoreDate", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                SharedPreferences.Editor date_editor = prefsDate.edit();
                int LatestScore = prefs.getInt("LatestScore", 0);
                editor.putInt(PlayerName.getText().toString(), LatestScore);
                date_editor.putString(PlayerName.getText().toString(), currentDate);
                editor.apply();date_editor.apply();

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
                c = PlayerList.get(i)+ "       "+prefsDate.getString(PlayerList.get(i), "no_date")+"       "+prefs.getInt(PlayerList.get(i), 0)+"\n";
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
            String Player = prefsPlayer.getString(key, "");
            if(!Player.isEmpty()) {
                PlayerList.add(Player);
            }
        }
    }
    public void Nettoyer(){
        prefsPlayer = getSharedPreferences("PlayerNames", MODE_PRIVATE);
        prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefs.edit();
        SharedPreferences.Editor editor2 = prefsPlayer.edit();
        editor1.clear();editor2.clear(); editor1.apply();editor2.apply();
    }
}
