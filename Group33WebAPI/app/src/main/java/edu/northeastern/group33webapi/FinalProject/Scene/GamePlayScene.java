package edu.northeastern.group33webapi.FinalProject.Scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.group33webapi.FinalProject.Audio.Audio;
import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;
import edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle.ObstacleManager;
import edu.northeastern.group33webapi.FinalProject.Controller.GyroScopicController;
import edu.northeastern.group33webapi.FinalProject.login.TopScore;
import edu.northeastern.group33webapi.R;

public class GamePlayScene implements Scene {
    private Dragon dragon;
    private Point dragonPoint;

    SceneManager sceneManager;

    private ObstacleManager obstacleManager;

    public boolean gameOver = false;

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

    public Audio audio;
    boolean flag;

//    private GestureDetector mGestureDetector;

//    GestureDetector.OnGestureListener onetouch;

//    public final int playState = 1;
//    public final int pauseState = 2;


    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }

    public GamePlayScene(SceneManager manager, Audio audio) {
        obstacleManager = new ObstacleManager(400, 350, 75, Color.LTGRAY,this);
        sceneManager = manager;
//        BitmapFactory bf = new BitmapFactory();
//        this.backGround = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.artboard11);

        this.audio = audio;
        flag = true;

        BitmapFactory bf1 = new BitmapFactory();
        this.backGround1 = bf1.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.b1);
        BitmapFactory bf2 = new BitmapFactory();
        this.backGround2 = bf2.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.b2);
        BitmapFactory bf3 = new BitmapFactory();
        this.backGround3 = bf3.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.b3);

        //initialize the dragon player
        dragon = new Dragon(new Rect(100, 100, 200, 200), Color.RED);

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
            audio.bgm.pause();
            return;
        }
        if (!gameOver) {
            if (audio.isSoundOn) {
                audio.bgm.start();
            }
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
                dragon.collideObst += 20;
                dragon.HP--; // if dragon hit wall lost one HP

                if (audio.isSoundOn) {
                    audio.obstacleAudio.start();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (dragon.HP == 0) {
                    gameOver = true; // GAME OVER
                }

//                top = dragon.score;
            }
            if (obstacleManager.coinCollide(dragon)) {
                dragon.collideCoin += 20;
                dragon.coinNum++;     // SCORE UPDATE BY PASS COIN
                dragon.score += 50;     // SCORE UPDATE BY PASS COIN

                if (audio.isSoundOn) {
                    audio.coinAudio.start();
                }

                if (dragon.coinNum == 10) {
                    dragon.getExtraHP += 20;
                    dragon.coinNum -= 10; // USE 100 COINS CHANGE ONE HP
                    dragon.HP++;
                    if (audio.isSoundOn) {
                        audio.addHpAudio.start();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        else if (flag){
            flag = false;
            if (audio.isSoundOn) {
                audio.gameOverAudio.start();
            }
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

        drawText(canvas, paint, "Score: " + dragon.score + "\n" + "Coins: " + dragon.coinNum + "\n" + "HP: " + dragon.HP, "topLeft");

        if (gameOver) {
            drawText(canvas, paint, "Game Over","center");
        }
        if (gameState % 2 == 0 && gameOver == false) {
            drawText(canvas, paint, "PAUSE","center");
        }
//        drawText(canvas, paint, "State" + gameState,"center");


        if (dragon.collideObst > 0) {
            dragon.collideObst--;
            drawText(canvas, paint, "HP - 1", "higherCenter");
        }
        if (dragon.getExtraHP > 0) {
            dragon.getExtraHP--;
            drawText(canvas, paint, "You get an extra Hit Points", "lowerCenter");
        }
        if (dragon.collideCoin > 0) {
            dragon.collideCoin--;
            drawText(canvas, paint, "Got a coin!", "center");
        }

    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

    }


    //draw the text "Game Over" in the center
    private void drawText(Canvas canvas, Paint paint, String text, String position) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = 0;
        float y = 0;
        switch (position) {
            case "center":
                x = cWidth / 2f - r.width() / 2f - r.left;
                y = cHeight / 2f + r.height() / 2f - r.bottom;
                break;
            case "topLeft":
                x = 0;
                y = r.height();
                break;
            case "lowerCenter":
                x = cWidth / 2f - r.width() / 2f - r.left;
                y = cHeight / 2f - r.height() / 2f - r.bottom;
                break;
            case "higherCenter":
                x = cWidth / 2f - r.width() / 2f - r.left;
                y = cHeight / 2f + 2 * r.height() - r.bottom;
                break;
        }
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
