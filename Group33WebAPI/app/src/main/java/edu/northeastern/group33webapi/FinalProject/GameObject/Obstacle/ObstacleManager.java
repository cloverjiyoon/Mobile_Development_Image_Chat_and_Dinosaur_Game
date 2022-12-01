package edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;

public class ObstacleManager {

    //higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean dragonCollide(Dragon dragon){
        for(Obstacle ob : obstacles){
            if(ob.dragonCollide(dragon))
                return true;
        }
        return false;
    }


    private void populateObstacles(){

        int currY = -5 * Constants.SCREEN_HEIGHT / 4;

        while(currY < 0){
            int xStart = (int) (Math.random() *(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    //update based on the frame
    public void update(){
        //fix the bug caused by leaving
        if(startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;

        int elapseTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) Math.sqrt(1 + (startTime - initTime)/1000.0) * Constants.SCREEN_HEIGHT/10000.0f;
        for(Obstacle ob : obstacles){
            ob.incrementY(speed * elapseTime);

        }

        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT){
            int xStart = (int)(Math.random() *(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
        }

    }

    public void draw(Canvas canvas){
        for(Obstacle ob : obstacles) {
            ob.draw(canvas);
        }
    }

}
