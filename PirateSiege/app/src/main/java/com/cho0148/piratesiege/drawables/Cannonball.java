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

public class Cannonball extends MyDrawable {
    private float speed;
    private double movementDegree;
    private Vector2D movementDegreeSinCos;
    private Vector2D goalPosition;
    private int damage;
    private boolean friendly;

    public Cannonball(Bitmap sprite, Vector2D position, Vector2D goalPosition, float speed, int damage, boolean friendly){
        super(sprite, position);

        this.goalPosition = goalPosition;
        this.movementDegree = this.computeMovementAngle(this.goalPosition);
        this.movementDegreeSinCos = this.getSinCos(this.movementDegree);
        this.speed = speed;
        this.damage = damage;
        this.friendly = friendly;
    }

    @Override
    public void update() {
        synchronized (this) {
            this.move();
            if(this.position.x < -this.sprite.getWidth() || this.position.y < -this.sprite.getHeight() || this.position.x > Game.getAreaSize().x + this.sprite.getWidth() || this.position.y > Game.getAreaSize().y + this.sprite.getHeight())
                this.destroy();
        }
    }

    public void move(){
        Vector2D movement = this.computeMovement(this.movementDegreeSinCos, this.speed);

        this.position.x -= movement.x;
        this.position.y -= movement.y;
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

    public int getDamage(){
        return this.damage;
    }

    public boolean isFriendly(){
        return this.friendly;
    }
}
