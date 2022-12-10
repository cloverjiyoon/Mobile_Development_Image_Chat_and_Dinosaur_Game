package edu.northeastern.group33webapi.FinalProject.GameObject.Dragon;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.FinalProject.GameObject.GameObject;
import edu.northeastern.group33webapi.FinalProject.login.registerActivity;
import edu.northeastern.group33webapi.R;

public class Dragon implements GameObject{

    private Rect rectangle;
    private int color;
    public int score;
    public int coinNum;
    public int HP;
    private DatabaseReference myDataBase;
    public int prevScore;
    private String userId;

    private Animation idle;
    private Animation walkRight;
    private Animation walkLeft;

    private AnimationManager animationManager;


    public Dragon(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;
        this.score = 0;
        this.coinNum = 0;
        this.HP = 3;
        this.prevScore = 0;

        myDataBase = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myDataBase.child("gameUsers").child(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                HashMap tempMap = (HashMap) task.getResult().getValue();
                if (tempMap == null) {
                    Log.e("firebase", "Empty data");
                    return;
                }
                this.prevScore = ((Long) Objects.requireNonNull(tempMap.get("score"))).intValue();
            }
        });


        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dinosaur);
        Bitmap walk1Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dinosaur);
        Bitmap walk2Img = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dinosaur);

        idle = new Animation(new Bitmap[]{idleImg}, 2);
        walkRight = new Animation(new Bitmap[]{walk1Img, walk2Img}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1Img = Bitmap.createBitmap(walk1Img, 0, 0, walk1Img.getWidth(), walk1Img.getHeight(), m, false);
        walk2Img = Bitmap.createBitmap(walk2Img, 0, 0, walk2Img.getWidth(), walk2Img.getHeight(), m, false);

        walkLeft = new Animation(new Bitmap[]{walk1Img, walk2Img}, 0.5f);

        animationManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft});

    }

    @Override
    public void draw(Canvas canvas) {
    /*    Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);*/
        animationManager.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        animationManager.update();
    }


    public void update(Point point) {
        float oldLeft = rectangle.left;

        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

        int state = 0;
        if(rectangle.left - oldLeft > 5)
            state = 1;
        else if(rectangle.left - oldLeft < -5)
            state = 2;

        animationManager.playAnim(state);
        animationManager.update();
    }

    public Rect getRectangle() {
        return rectangle;
    }
}
