package edu.northeastern.group33webapi.FinalProject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;

import edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle.ObstacleManager;
import edu.northeastern.group33webapi.FinalProject.Scene.GamePlayScene;
import edu.northeastern.group33webapi.FinalProject.Scene.SceneManager;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private static final String DEBUG_TAG = "Gestures";
    private MainThread thread;
    SceneManager sceneManager;


    public GamePanel(Context context) {
        super(context);


        getHolder().addCallback(this);

//        thread = new MainThread(getHolder(), this);

        Constants.CURRENT_CONTEXT = context;

        sceneManager = new SceneManager();

        setFocusable(true);


    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread = new MainThread(getHolder(), this, Constants.CURRENT_CONTEXT);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;

        //when you re-enter
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (Exception e) {
                e.printStackTrace();
            }

            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);
        GamePlayScene gamePlayScene = (GamePlayScene) sceneManager.getScenes().get(sceneManager.ACTIVE_SCENE);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                gamePlayScene.gameState++;
                gamePlayScene.getObstacleManager().gameState++;
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }

//        return super.onTouchEvent(event);
    }

    public void update() {
        sceneManager.update();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        sceneManager.draw(canvas);


    }


}
