package edu.northeastern.group33webapi.FinalProject.Scene;

import static android.view.MotionEvent.ACTION_BUTTON_PRESS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.logging.Handler;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;
import edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle.ObstacleManager;
import edu.northeastern.group33webapi.FinalProject.Controller.GyroScopicController;
import edu.northeastern.group33webapi.FinalProject.login.TopScore;
import edu.northeastern.group33webapi.FinalProject.login.registerActivity;
import edu.northeastern.group33webapi.FinalProjectActivity;
import edu.northeastern.group33webapi.R;

public class GamePlayScene implements Scene {
    private Dragon dragon;
    private Point dragonPoint;

    SceneManager sceneManager;

    private ObstacleManager obstacleManager;

    private boolean gameOver = false;

    private Rect r = new Rect();

    private GyroScopicController gyroScopicController;

    //track the time between frames
    private long frameTime;
    private TopScore topScore;

    public int gameState = 1;
//    public Bitmap backGround;
    public Bitmap backGround1;
    public Bitmap backGround2;
    public Bitmap backGround3;


//    private GestureDetector mGestureDetector;

//    GestureDetector.OnGestureListener onetouch;

//    public final int playState = 1;
//    public final int pauseState = 2;


    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }

    public GamePlayScene(SceneManager manager) {
        obstacleManager = new ObstacleManager(400, 350, 75, Color.LTGRAY);
        sceneManager = manager;
//        BitmapFactory bf = new BitmapFactory();
//        this.backGround = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.artboard11);

        BitmapFactory bf1 = new BitmapFactory();
        this.backGround1 = bf1.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.b1);
        BitmapFactory bf2 = new BitmapFactory();
        this.backGround2 = bf2.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.b2);
        BitmapFactory bf3 = new BitmapFactory();
        this.backGround3 = bf3.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.b3);

        //initialize the dragon player
        dragon = new Dragon(new Rect(100, 100, 270, 270), Color.RED);

        dragonPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2);
        dragon.update(dragonPoint);

        gyroScopicController = new GyroScopicController();
        gyroScopicController.register();

        frameTime = System.currentTimeMillis();
//        topScore = new TopScore();


    }


    @Override
    public void update() {

        // frameTime = System.currentTimeMillis();
        if (gameState % 2 == 0) {
            return;
        }
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = Constants.FT;
            System.out.printf("%d %d\n", elapsedTime, System.currentTimeMillis());
            frameTime = System.currentTimeMillis();

            if (gyroScopicController.getOrientation() != null && gyroScopicController.getStartOrientation() != null) {
//                float pitch = gyroScopicController.getOrientation()[1] - gyroScopicController.getStartOrientation()[1];
//                float roll = gyroScopicController.getOrientation()[2] - gyroScopicController.getStartOrientation()[2];

                float pitch = gyroScopicController.getOrientation()[1];
                float roll = gyroScopicController.getOrientation()[2];
                float temp = gyroScopicController.getOrientation()[0];

                float[] gyro = gyroScopicController.getGyroOutput();

                //   System.out.println("GYRO  " + gyro[0] * 100000 + "   " + gyro[1] * 100000 + "   " + gyro[2] * 10000);

                float xSpeed = 2 * gyro[1] * Constants.SCREEN_WIDTH / 500f;
                float ySpeed = 2 * -gyro[1] * Constants.SCREEN_HEIGHT / 1000f;

                dragonPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
//                dragonPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? xSpeed*elapsedTime : 0;

            }

            dragonPoint.x = Math.max(0, dragonPoint.x);
            dragonPoint.x = Math.min(dragonPoint.x, Constants.SCREEN_WIDTH);

            dragonPoint.y = Math.max(0, dragonPoint.y);
            dragonPoint.y = Math.min(dragonPoint.y, Constants.SCREEN_HEIGHT);

            dragon.update(dragonPoint);
            obstacleManager.update();

            if (obstacleManager.dragonCollide(dragon)) {
                dragon.HP--; // if dragon hit wall lost one HP
                if (dragon.HP == 0) {
                    gameOver = true; // GAME OVER
                }
//                top = dragon.score;

            }
            if (obstacleManager.coinCollide(dragon)) {
                dragon.coinNum++;     // SCORE UPDATE BY PASS COIN
                dragon.score += 50;     // SCORE UPDATE BY PASS COIN
                if (dragon.coinNum == 100) {
                    dragon.coinNum -= 100; // USE 100 COINS CHANGE ONE HP
                    dragon.HP++;
                }
            }
            ;
            if (obstacleManager.scoreCollide(dragon)) {
                dragon.score++;     // SCORE UPDATE BY PASS WALL
            }


//            if (gameOver == true){
//                new AlertDialog.Builder(Constants.CURRENT_CONTEXT)
//                        .setTitle("Game Over")
//                        .setMessage("GAMEOVER,YOUR SCORE IS" + dragon.score)
//                        .setPositiveButton("RESTART",new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int which){
//                        dragon = new Dragon(new Rect(100, 100, 200, 200), Color.RED);
//                        gameOver = false;
//                    }
//                }).show();
//            }
//
        }
        else {
            if (dragon.score > dragon.prevScore){
                DatabaseReference myDataBase = FirebaseDatabase.getInstance().getReference();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                myDataBase.child("gameUsers").child(userId).child("score").setValue(dragon.score);
            }
        }
    }

    private void drawBackground(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        backGround1 = Bitmap.createScaledBitmap(backGround1,width,height,false);
        backGround2 = Bitmap.createScaledBitmap(backGround2,width,height,false);
        backGround3 = Bitmap.createScaledBitmap(backGround3,width,height,false);

        canvas.drawBitmap(backGround1,0,0,null);
        canvas.drawBitmap(backGround2,0,0,null);
        canvas.drawBitmap(backGround3,0,0,null);
    }


    @Override
    public void draw(Canvas canvas) {

        drawBackground(canvas);


//        backGround = Bitmap.createScaledBitmap(backGround,width,height,false);
//        canvas.drawBitmap(backGround,0,0,null);

        dragon.draw(canvas);
        obstacleManager.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(70);
        paint.setColor(Color.YELLOW);
        Typeface typeFace = paint.getTypeface();
        paint.setTypeface(typeFace.create("Arial",typeFace.BOLD));

        drawTopLeftText(canvas, paint, "Score: " + dragon.score + "\n" + "Coins: " + dragon.coinNum + "\n" + "HP: " + dragon.HP);

        if (gameOver) {
            drawCenterText(canvas, paint, "Game Over");
        }
        if (gameState % 2 == 0 && gameOver == false) {
            drawCenterText(canvas, paint, "PAUSE");
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

    }


    //draw the text "Game Over" in the center
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    private void drawTopLeftText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = 0;
        float y = r.height();
        canvas.drawText(text, x, y, paint);
    }

    GestureDetector.OnGestureListener listener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };
}
