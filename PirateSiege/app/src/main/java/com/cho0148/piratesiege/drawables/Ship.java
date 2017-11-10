package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.Vector2D;

/**
 * Created by chole on 10.11.2017.
 */

public class Ship extends MyDrawable {
    private Bitmap sprite;
    private Vector2D position;
    private Vector2D defaultSpriteSize;
    private Vector2D defaultPosition;
    private Paint paint;
    private float speed;
    private float defaultSpeed;
    private double movementDegree;
    private Vector2D goalPosition;
    private ShipSpriteVariant variant;

    public static enum ShipSpriteVariant{CLEAR, PIRATE, CRUSADER, WARRIOR, HORSE, BONE};

    public Ship(ShipSpriteVariant variant, Vector2D position, float speed){
        this.variant = variant;
        this.sprite = getSpriteFromVariant(this.variant);
        this.position = position;
        this.defaultPosition = this.position;
        this.goalPosition = null;
        this.defaultSpeed = speed;
        this.speed = speed;

        this.defaultSpriteSize = new Vector2D(this.sprite.getWidth(), this.sprite.getHeight());
        this.paint = new Paint();
    }

    public static Bitmap getSpriteFromVariant(ShipSpriteVariant variant){
        String filename = "";
        switch(variant){
            case CLEAR:
                filename = "ship_1";
                break;
            case PIRATE:
                filename = "ship_2";
                break;
            case CRUSADER:
                filename = "ship_3";
                break;
            case WARRIOR:
                filename = "ship_4";
                break;
            case HORSE:
                filename = "ship_5";
                break;
            case BONE:
                filename = "ship_6";
                break;
        }
        int id = Game.getContext().getResources().getIdentifier(filename, "drawable", "com.cho0148.piratesiege");
        return BitmapFactory.decodeResource(Game.getContext().getResources(), id);
    }

    @Override
    public void setScale(Vector2D scale) {
        this.sprite = Bitmap.createScaledBitmap(this.sprite, (int)(Math.ceil(this.defaultSpriteSize.x * scale.x)), (int)(Math.ceil(this.defaultSpriteSize.y * scale.y)), true);
        this.position.x = (float)(Math.floor(this.defaultPosition.x * scale.x));
        this.position.y = (float)(Math.floor(this.defaultPosition.y * scale.y));
        this.speed = this.defaultSpeed * scale.x;
    }

    @Override
    public void update() {
        this.move();
    }

    public void move(){
        if(this.goalPosition == null)
            return;

        Vector2D movement = new Vector2D(Math.sin(this.movementDegree) * this.speed, Math.cos(this.movementDegree) * this.speed);
        if(Math.abs(this.position.x - this.goalPosition.x) < this.speed && (this.position.y - this.goalPosition.y) < this.speed)
            return;

        this.position.x += movement.x;
        this.position.y += movement.y;
    }

    public void setGoalPosition(Vector2D goalPosition){
        this.goalPosition = goalPosition;
        this.movementDegree = Math.atan((this.goalPosition.x - this.position.x) / (this.goalPosition.y - this.position.y));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.rotate(-(float)(this.movementDegree*180/Math.PI), this.position.x, this.position.y);
        canvas.drawBitmap(this.sprite, this.position.x, this.position.y, this.paint);
        canvas.rotate((float)(this.movementDegree*180/Math.PI), this.position.x, this.position.y);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
