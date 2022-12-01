package edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;
import edu.northeastern.group33webapi.FinalProject.GameObject.GameObject;

public class Obstacle implements GameObject {
    private  Rect rectangle;
    private int color;
    private  Rect rectangle2;


    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;

        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap){
        this.rectangle = new Rect(0, startY, startX, startY + rectHeight);
        this.color = color;
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);

    }


    public boolean dragonCollide(Dragon dragon){
        if(rectangle.contains(dragon.getRectangle().left, dragon.getRectangle().top) ||
                rectangle.contains(dragon.getRectangle().right, dragon.getRectangle().top) ||
                rectangle.contains(dragon.getRectangle().right, dragon.getRectangle().bottom) ||
                rectangle.contains(dragon.getRectangle().left, dragon.getRectangle().bottom)
        )
            return true;
        return false;



    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);

    }

    @Override
    public void update() {

    }

}
