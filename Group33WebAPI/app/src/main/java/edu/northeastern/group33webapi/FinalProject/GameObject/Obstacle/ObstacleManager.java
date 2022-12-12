package edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;
import edu.northeastern.group33webapi.FinalProject.GamePanel;
import edu.northeastern.group33webapi.FinalProject.Scene.GamePlayScene;
import edu.northeastern.group33webapi.FinalProject.Scene.SceneManager;

public class ObstacleManager {

    //higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;
    private GamePlayScene gamePlayScene;


    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color, GamePlayScene gamePlayScene) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;
        this.gamePlayScene = gamePlayScene;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean dragonCollide(Dragon dragon) {
        for (Obstacle ob : obstacles) {
            if (ob.dragonCollide(dragon) && !ob.isHashit()) {
                ob.setHashit();
                ob.color = Color.RED;
                return true;
            }
        }
        return false;
    }

    public boolean coinCollide(Dragon dragon) {
        for (Obstacle ob : obstacles) {
            if (ob.coinCollide(dragon))
                return true;
        }
        return false;
    }

    public boolean scoreCollide(Dragon dragon) {
        for (Obstacle ob : obstacles) {
            if (ob.scoreCollide(dragon))
                return true;
        }
        return false;
    }

    private void populateObstacles() {

        int currY = -5 * Constants.SCREEN_HEIGHT / 4;

        while (currY < 0) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    //update based on the frame
    public void update() {
        //fix the bug caused by leaving

        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;

        if ( this.gamePlayScene.gameState == 2) {
            return;
        }
        int elapseTime = Constants.FT;
        startTime = System.currentTimeMillis();
        float speed = (float) Math.sqrt(1 + (startTime - initTime) / 1000.0) * Constants.SCREEN_HEIGHT / 10000.0f * 0.8f;
        for (Obstacle ob : obstacles) {
            ob.incrementY(speed * elapseTime);
            ob.incrementX(0.35F * speed * elapseTime);

        }

        if (obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
        }
        for (Obstacle ob : obstacles) {
            ob.update();
        }

    }

    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacles) {
            ob.draw(canvas);
        }
    }

}
