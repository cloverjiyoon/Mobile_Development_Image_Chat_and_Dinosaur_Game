package edu.northeastern.group33webapi.FinalProject.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.group33webapi.FinalProject.Ranking.RankingActivity;
import edu.northeastern.group33webapi.FinalProjectActivity;
import edu.northeastern.group33webapi.R;

public class gameStartActivity extends AppCompatActivity {
    Button startGame, showRanking;
    TextView HiScore;
    TopScore topScore;
    Switch sound;
    boolean isSoundOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamestartpage);
        topScore = new TopScore(this);
        startGame = (Button) findViewById(R.id.beginingGame);
        showRanking = (Button) findViewById(R.id.showRank);
        sound = (Switch) findViewById(R.id.soundSwitch);

        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FinalProjectActivity.class);
                intent.putExtra("isSoundOn", isSoundOn);
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


        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 处理用户点击开关时的事件
                if (isChecked) {
                    // 开关被打开
                    isSoundOn = true;
                } else {
                    // 开关被关闭
                    isSoundOn = false;
                }
            }
        });




    }
}
