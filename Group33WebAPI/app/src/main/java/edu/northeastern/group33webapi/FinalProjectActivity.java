package edu.northeastern.group33webapi;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH = dm.widthPixels;

        Audio audio = new Audio(this);
        GamePanel gp = new GamePanel(this, audio);
        setContentView(gp);

    }


}
