package edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Dragon;
import edu.northeastern.group33webapi.FinalProject.GameObject.GameObject;

public class Obstacle implements GameObject {
    private Rect rectLeft;
    private int color;
    private Rect rectRight;
    private Coin coin;
    private Rect wholeRect;

    public void incrementY(float y) {
        rectLeft.top += y;
        rectLeft.bottom += y;

        rectRight.top += y;
        rectRight.bottom += y;
        wholeRect.top += y;
        wholeRect.bottom += y;
        if (this.coin != null) {
            this.coin.getRectangle().top += y;
            this.coin.getRectangle().bottom += y;
        }
    }

    public Rect getRectangle() {
        return rectLeft;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.rectLeft = new Rect(0, startY, startX, startY + rectHeight);
        this.color = color;
        this.rectRight = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
        this.wholeRect = new Rect(0, startY, Constants.SCREEN_WIDTH, startY + rectHeight);

        int left = (int) (Math.random() *(Constants.SCREEN_WIDTH));
        this.coin = new Coin(new Rect(left, startY + 150, left + 100, startY + 250),Color.RED);
    }


    public boolean dragonCollide(Dragon dragon) {
        if (rectLeft.contains(dragon.getRectangle().left, dragon.getRectangle().top) ||
                rectLeft.contains(dragon.getRectangle().right, dragon.getRectangle().top) ||
                rectLeft.contains(dragon.getRectangle().right, dragon.getRectangle().bottom) ||
                rectLeft.contains(dragon.getRectangle().left, dragon.getRectangle().bottom) ||
                rectRight.contains(dragon.getRectangle().left, dragon.getRectangle().top) ||
                rectRight.contains(dragon.getRectangle().right, dragon.getRectangle().top) ||
                rectRight.contains(dragon.getRectangle().right, dragon.getRectangle().bottom) ||
                rectRight.contains(dragon.getRectangle().left, dragon.getRectangle().bottom)

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
        canvas.drawRect(rectRight, paint);
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
