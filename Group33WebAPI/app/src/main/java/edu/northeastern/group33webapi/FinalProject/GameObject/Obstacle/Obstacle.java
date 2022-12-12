package edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.android.gms.common.internal.FallbackServiceBroker;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;
import edu.northeastern.group33webapi.FinalProject.GameObject.GameObject;

public class Obstacle implements GameObject {
    private Rect rectLeft;
    public int color;
    private Coin coin;
    private Rect wholeRect;
    private boolean hashit;
    private float leftMovableDir = 1.0F;

    public void setHashit() {
        this.hashit = true;
    }

    public boolean isHashit() {
        return this.hashit;
    }

    public void incrementY(float y) {
        rectLeft.top += y;
        rectLeft.bottom += y;


        wholeRect.top += y;
        wholeRect.bottom += y;
        if (this.coin != null) {
            this.coin.getRectangle().top += y;
            this.coin.getRectangle().bottom += y;
        }
    }

    public void incrementX(float x) {
        if (rectLeft.right + x >= Constants.SCREEN_WIDTH) {
            leftMovableDir = -1F;
        }
        if (rectLeft.left <= 0){
            leftMovableDir = 1F;
        }

        rectLeft.left = (int) (rectLeft.left+ leftMovableDir*x);
        rectLeft.right = (int) (rectLeft.right+leftMovableDir*x);

    }




    public Rect getRectangle() {
        return rectLeft;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.rectLeft = new Rect(0, startY, startX, startY + rectHeight);
        this.color = color;
        this.wholeRect = new Rect(0, startY, Constants.SCREEN_WIDTH, startY + rectHeight);

        int left = (int) (Math.random() *(Constants.SCREEN_WIDTH));
        this.coin = new Coin(new Rect(left, startY + 150, left + 100, startY + 250),Color.RED);
        this.hashit = false;
    }


    public boolean dragonCollide(Dragon dragon) {
        if (rectLeft.contains(dragon.getRectangle().left, dragon.getRectangle().top) ||
                rectLeft.contains(dragon.getRectangle().right, dragon.getRectangle().top) ||
                rectLeft.contains(dragon.getRectangle().right, dragon.getRectangle().bottom) ||
                rectLeft.contains(dragon.getRectangle().left, dragon.getRectangle().bottom)

        ) {
            return true;
        }
        return false;
    }

    public boolean scoreCollide(Dragon dragon) {
        if (wholeRect.contains(dragon.getRectangle().left, dragon.getRectangle().top) ||
                wholeRect.contains(dragon.getRectangle().right, dragon.getRectangle().top) ||
                wholeRect.contains(dragon.getRectangle().right, dragon.getRectangle().bottom) ||
                wholeRect.contains(dragon.getRectangle().left, dragon.getRectangle().bottom)
        ) {
            return true;
        }
        return false;
    }

    public boolean coinCollide(Dragon dragon) {
        if (this.coin == null) {
            return false;
        }
        Rect rect = this.coin.getRectangle();
        if (rect.contains(dragon.getRectangle().left, dragon.getRectangle().top) ||
                rect.contains(dragon.getRectangle().right, dragon.getRectangle().top) ||
                rect.contains(dragon.getRectangle().right, dragon.getRectangle().bottom) ||
                rect.contains(dragon.getRectangle().left, dragon.getRectangle().bottom)
        ) {
            this.coin = null;
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectLeft, paint);
        if (this.coin != null) {
            this.coin.draw(canvas);
        }
    }

    @Override
    public void update() {
        if (this.coin != null) {
            this.coin.update();
        }
    }

}
