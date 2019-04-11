package com.example.breakout;

import android.content.Intent;
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
    Button AddScoreButton;
    TextView LastScore, ScoreBoard, WinLose;
    EditText PlayerName;
    List<String> PlayerList;
    String currentDate;
    SharedPreferences pref, prefs, prefsPlayer, prefsDate;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor date_editor;
    SharedPreferences.Editor player_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        PlayerList = new ArrayList<String>();
        prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
        prefsPlayer = getSharedPreferences("PlayerNames", MODE_PRIVATE);
        prefsDate = getSharedPreferences("ScoreDate", MODE_PRIVATE);
        pref = getSharedPreferences("ShowButton", MODE_PRIVATE);
        editor = prefs.edit();
        date_editor = prefsDate.edit();
        player_editor = prefsPlayer.edit();

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        //Config des Widgets
        WinLose = (TextView) findViewById(R.id.activity_score_winlose);
        AddScoreButton = (Button) findViewById(R.id.activity_score_addScore);
        LastScore = (TextView) findViewById(R.id.activity_score_lastscore);
        ScoreBoard = (TextView) findViewById(R.id.activity_score_scoreboard);
        PlayerName = (EditText) findViewById(R.id.activity_score_playername);
        if(getIntent().getBooleanExtra("Lose", false)){
            WinLose.setText("Vous avez perdu...");
        }else{
            WinLose.setText("Vous avez gagné !!");
        }
        AddScoreButton.setVisibility(View.VISIBLE);
        PlayerName.setVisibility(View.VISIBLE);
        LastScore.setVisibility(View.VISIBLE);
        WinLose.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if(!(intent.getBooleanExtra("ShowButton",true))){
            AddScoreButton.setVisibility(View.INVISIBLE);
            PlayerName.setVisibility(View.INVISIBLE);
            LastScore.setVisibility(View.INVISIBLE);
            WinLose.setVisibility(View.INVISIBLE);
        }
        RecupList();
        DisplayScore();
        LastScore.setText("Votre score : "+Integer.toString(prefs.getInt("LatestScore", 0)));
        //Sauvegarde du score actuel
        AddScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                SaveList();
            }
        });

    }
    public void addPlayer(String n){
        int LatestScore = prefs.getInt("LatestScore", 0);
        boolean replace = false;
        for(int i =0; i<PlayerList.size(); i++){
            if(PlayerList.get(i).equals(n)){
                replace = true;
                if(prefs.getInt(PlayerList.get(i), 0) < LatestScore){
                    editor.putInt(n, LatestScore);
                    date_editor.putString(PlayerName.getText().toString(), currentDate);
                }
            }
        }
        if(replace==false){
            PlayerList.add(n);
            editor.putInt(n, LatestScore);
            date_editor.putString(PlayerName.getText().toString(), currentDate);
        }
        editor.apply(); date_editor.apply();

    }
    public void SortList(){
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
        String chaine = "";
        String c;
        ScoreBoard.setText("");
        for(int i=0; i<PlayerList.size(); i++){
            if(PlayerList.get(i).isEmpty()){
                c = "---"+"\n";
            }else{
                c = PlayerList.get(i)+ "\t - \t"+prefsDate.getString(PlayerList.get(i), "no_date")+"\t - \t"+prefs.getInt(PlayerList.get(i), 0)+"\n";
            }
            chaine += c;
        }
        ScoreBoard.setText(chaine);
    }
    public void SaveList(){
        for(int i=0; i<PlayerList.size(); i++){
            String key ="best"+Integer.toString(i+1);
            player_editor.putString(key, PlayerList.get(i));
            player_editor.apply();
        }
    }
    public void RecupList(){
        for(int i=0; i<5; i++){
            String key = "best"+Integer.toString(i+1);
            String Player = prefsPlayer.getString(key, "");
            if(!Player.isEmpty()) {
                PlayerList.add(Player);
            }
        }
    }
    public void Nettoyer(){
        editor.clear();date_editor.clear(); player_editor.clear();
        editor.apply();date_editor.apply(); player_editor.apply();
    }
}
