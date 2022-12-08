package edu.northeastern.group33webapi.FinalProject.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.group33webapi.FinalProject.Ranking.RankingActivity;
import edu.northeastern.group33webapi.FinalProjectActivity;
import edu.northeastern.group33webapi.R;

public class gameStartActivity extends AppCompatActivity {
    Button startGame, showRanking;
    TextView HiScore;
    TopScore topScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamestartpage);
        topScore = new TopScore(this);
        startGame = (Button) findViewById(R.id.beginingGame);
        showRanking = (Button) findViewById(R.id.showRank);
//        HiScore = findViewById(R.id.TopScore);

//        if (String.valueOf(topScore.getTopScore()) == "") {
//            HiScore.setText("00000");
//        }else {
//            HiScore.setText(String.valueOf(topScore.getTopScore()));
//        }

        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FinalProjectActivity.class);
                startActivity(intent);
            }
    });

        showRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(intent);
            }
        });



    }
}
