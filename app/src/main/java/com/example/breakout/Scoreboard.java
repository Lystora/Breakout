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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard extends AppCompatActivity {
    Button Minus, Plus, SaveButton;
    TextView ScoreText, ScoreBoard;
    EditText PlayerName;
    List<String> PlayerList;
    int Value = 0;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        PlayerList = new ArrayList<String>();
        //Config des Widgets
        Minus = (Button) findViewById(R.id.activity_score_minus);
        Plus = (Button) findViewById(R.id.activity_score_plus);
        SaveButton = (Button) findViewById(R.id.activity_score_save);
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
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("PlayerScore", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(PlayerName.getText().toString(), Value);
                editor.apply();


                if(PlayerList.size()==5){
                    addPlayer(PlayerName.getText().toString());
                    SortList();
                    removePlayer();
                }else{
                    addPlayer(PlayerName.getText().toString());
                    SortList();
                }
                DisplayScore();
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
}
