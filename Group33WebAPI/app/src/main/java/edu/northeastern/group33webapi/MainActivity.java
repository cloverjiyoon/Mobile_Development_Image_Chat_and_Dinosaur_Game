package edu.northeastern.group33webapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonWeatherAPI = findViewById(R.id.button1);
        Button buttonFirebase = findViewById(R.id.firebase);
        Button buttonAbout = findViewById(R.id.about);
        Button buttonFinalProject = findViewById(R.id.finalProject);

        buttonWeatherAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        buttonFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FirebaseActivity.class);
                startActivity(intent);
            }
        });

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                String text = "Team : TrueLoop\n\n" + "Jiyoon(Clover) Jeong : jeong.jiy@northeastern.edu\n" +
                        "\n" +
                        "Yachen(Carl) Yin : yin.yac@northeastern.edu\n" +
                        "TjuAachen\n" +
                        "\n" +
                        "Jiajie(Danny) Yin : yin.jiaj@northeastern.edu\n" +
                        "Danny-Yin\n" +
                        "\n" +
                        "Lei(ken) Li : li.lei1@northeastern.edu\n" +
                        "tiaowudepangxie\n" +
                        "\n" +
                        "Zhu Wang : wang.zhu1@northeastern.edu\n" +
                        "ZhuWang1112\n";
                Toast.makeText(context, text, duration).show();

            }
        });

        buttonFinalProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FinalProjectActivity.class);
                startActivity(intent);
            }
        });
    }
}