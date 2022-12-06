package edu.northeastern.group33webapi.FinalProject.Scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;
import edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle.ObstacleManager;
import edu.northeastern.group33webapi.FinalProject.Controller.GyroScopicController;

public class GamePlayScene implements Scene{
    private Dragon dragon;
    private Point dragonPoint;

    SceneManager sceneManager;

    private ObstacleManager obstacleManager;

    private boolean gameOver = false;

    private Rect r = new Rect();

    private GyroScopicController gyroScopicController;

    //track the time between frames
    private long frameTime;

    public GamePlayScene(SceneManager manager){
        obstacleManager = new ObstacleManager(600, 350, 75, Color.LTGRAY);
        sceneManager = manager;

        //initialize the dragon player
        dragon = new Dragon(new Rect(100, 100, 200, 200), Color.RED);

        dragonPoint = new Point(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2);
        dragon.update(dragonPoint);

        gyroScopicController = new GyroScopicController();
        gyroScopicController.register();

        frameTime = System.currentTimeMillis();



    }


    @Override
    public void update() {
        if(!gameOver){
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();

            if(gyroScopicController.getOrientation() != null && gyroScopicController.getStartOrientation() != null){
//                float pitch = gyroScopicController.getOrientation()[1] - gyroScopicController.getStartOrientation()[1];
//                float roll = gyroScopicController.getOrientation()[2] - gyroScopicController.getStartOrientation()[2];

                float pitch = gyroScopicController.getOrientation()[1];
                float roll = gyroScopicController.getOrientation()[2];
                float temp = gyroScopicController.getOrientation()[0];

                float[] gyro = gyroScopicController.getGyroOutput();

                System.out.println("GYRO  " + gyro[0] * 100000 + "   " + gyro[1] * 100000 + "   " + gyro[2] * 10000);

                float xSpeed = 2 * gyro[1] * Constants.SCREEN_WIDTH/500f;
                float ySpeed = 2 * -gyro[1] * Constants.SCREEN_HEIGHT/1000f;

                dragonPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                dragonPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? xSpeed*elapsedTime : 0;

            }

            dragonPoint.x = Math.max(0, dragonPoint.x);
            dragonPoint.x = Math.min(dragonPoint.x, Constants.SCREEN_WIDTH);

            dragonPoint.y = Math.max(0, dragonPoint.y);
            dragonPoint.y = Math.min(dragonPoint.y, Constants.SCREEN_HEIGHT);

            dragon.update(dragonPoint);
            obstacleManager.update();

            if(obstacleManager.dragonCollide(dragon)) {
                gameOver = true;

            }
            if(obstacleManager.coinCollide(dragon)) {
                dragon.coinNum++;
            };
            if (obstacleManager.scoreCollide(dragon)) {
                dragon.score++;
            }
        }


    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.rgb(0, 135, 62));
        dragon.draw(canvas);
        obstacleManager.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);

        drawTopLeftText(canvas, paint, "Score: " + dragon.score + "\n" + "Coins: " + dragon.coinNum);

        if(gameOver){
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

    }

    //draw the text "Game Over" in the center
    private void drawCenterText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
    private void drawTopLeftText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = 0;
        float y = r.height();
        canvas.drawText(text, x, y, paint);
    }
}
