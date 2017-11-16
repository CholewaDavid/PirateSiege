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

public class Cannonball extends MyDrawable {
    private Bitmap sprite;
    private Vector2D position;
    private Paint paint;
    private float speed;
    private float defaultSpeed;
    private double movementDegree;
    private Vector2D goalPosition;
    private int damage;

    public Cannonball(Vector2D position, Vector2D goalPosition, float speed, int damage){
        this.sprite = BitmapFactory.decodeResource(Game.getContext().getResources(), (Game.getContext().getResources().getIdentifier("cannonball", "drawable", "com.cho0148.piratesiege")));
        this.position = position;
        this.goalPosition = goalPosition;
        this.movementDegree = Math.atan((this.goalPosition.x - this.position.x) / (this.goalPosition.y - this.position.y));
        this.defaultSpeed = speed;
        this.speed = speed;
        this.damage = damage;
        this.paint = new Paint();
    }

    @Override
    public void setScale(Vector2D scale) {

    }

    @Override
    public void update() {
        synchronized (this) {
            this.move();
        }
    }

    public void move(){
        Vector2D movement = new Vector2D(Math.sin(this.movementDegree) * this.speed, Math.cos(this.movementDegree) * this.speed);
        if(Math.abs(this.position.x - this.goalPosition.x) < this.speed && (this.position.y - this.goalPosition.y) < this.speed)
            return;

        this.position.x += movement.x;
        this.position.y += movement.y;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        synchronized (this) {
            canvas.rotate(-(float) (this.movementDegree * 180 / Math.PI), this.position.x, this.position.y);
            canvas.drawBitmap(this.sprite, this.position.x, this.position.y, this.paint);
            canvas.rotate((float) (this.movementDegree * 180 / Math.PI), this.position.x, this.position.y);
        }
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
