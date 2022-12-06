package edu.northeastern.group33webapi.FinalProject.GameObject.Obstacle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.Animation;
import edu.northeastern.group33webapi.FinalProject.GameObject.Dragon.AnimationManager;
import edu.northeastern.group33webapi.FinalProject.GameObject.GameObject;
import edu.northeastern.group33webapi.R;

public class Coin implements GameObject {


    private Rect rectangle;
    private int color;
    private Bitmap c1;

    private Animation idle;

    private AnimationManager animationManager;

    public Coin(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;


        BitmapFactory bf = new BitmapFactory();
        Bitmap coin1Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.coin1);
        Bitmap coin2Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.coin2);
        Bitmap coin3Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.coin3);
        Bitmap coin4Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.coin4);
        Bitmap coin5Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.coin5);
        Bitmap coin6Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.coin6);

        idle = new Animation(new Bitmap[]{coin1Img, coin2Img, coin3Img, coin4Img, coin5Img, coin6Img}, 0.5f);

        animationManager = new AnimationManager(new Animation[]{idle});
//        c1 = coin3Img;
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawBitmap(c1, null, this.rectangle, new Paint());
//        System.out.println(rectangle.toString());
//        System.out.println("*************");
        animationManager.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        animationManager.playAnim(0);
        animationManager.update();
    }

    public Rect getRectangle() {
        return rectangle;
    }
}
