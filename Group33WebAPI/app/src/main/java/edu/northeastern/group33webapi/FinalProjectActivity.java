package edu.northeastern.group33webapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.group33webapi.FinalProject.Audio.Audio;
import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GamePanel;
import edu.northeastern.group33webapi.FinalProject.Scene.GamePlayScene;

public class FinalProjectActivity extends AppCompatActivity {
    Audio audio;
    GamePanel gp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH = dm.widthPixels;

        audio = new Audio(this, getIntent().getBooleanExtra("isSoundOn", true));
        gp = new GamePanel(this, audio);
        setContentView(gp);

    }
    @Override
    public void onBackPressed() {

//        audio.bgm.stop();
        GamePlayScene gamePlayScene = (GamePlayScene) gp.sceneManager.getScenes().get(gp.sceneManager.ACTIVE_SCENE);

        gamePlayScene.gameState = 2;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit")
                .setMessage("Are you sure you want to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gamePlayScene.gameState = 1;
//                        audio.bgm.start();
                    }
                });

        builder.show();
    }





}
