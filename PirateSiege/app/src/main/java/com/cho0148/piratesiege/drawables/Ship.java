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

public class Ship extends MyDrawable {
    private float speed;
    private double movementDegree;
    private Vector2D movementDegreeSinCos;
    private Vector2D goalPosition;
    private ShipSpriteVariant variant;
    private float range;
    private long nextShotTime;
    private int shotCooldown;

    public enum ShipSpriteVariant{CLEAR, PIRATE, CRUSADER, WARRIOR, HORSE, BONE};

    public Ship(ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown){
        super(getSpriteFromVariant(variant), position);
        this.variant = variant;
        this.goalPosition = null;
        this.speed = speed;
        this.range = range;
        this.shotCooldown = shotCooldown;
        this.nextShotTime = System.currentTimeMillis() + this.shotCooldown;
        this.movementDegree = 0;
        this.movementDegreeSinCos = new Vector2D();
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
    public void update() {
        synchronized (this) {
            if (this.nextShotTime < System.currentTimeMillis()) {
                if (Math.abs(this.position.x - this.goalPosition.x) < this.range && (this.position.y - this.goalPosition.y) < this.range)
                    this.shoot();
            }
            this.move();
        }
    }

    public void shoot(){
        Vector2D pos = new Vector2D(this.position);
        pos.x += this.movementDegreeSinCos.x * this.sprite.getWidth();
        pos.y += this.movementDegreeSinCos.y * this.sprite.getHeight() / 2;
        DrawableFactory.createCannonball(pos, new Vector2D(this.goalPosition), 20, 10);
        this.nextShotTime = System.currentTimeMillis() + this.shotCooldown;
    }

    public void move(){
        if(this.goalPosition == null)
            return;

        Vector2D movement = new Vector2D(this.movementDegreeSinCos.x * this.speed, this.movementDegreeSinCos.y * this.speed);
        if(Math.abs(this.position.x - this.goalPosition.x) < this.range &&
                Math.abs(this.position.y - this.goalPosition.y) < this.range) {
            this.destroy();
            return;
        }

        this.position.x += movement.x;
        this.position.y += movement.y;
    }

    public void setGoalPosition(Vector2D goalPosition){
        this.goalPosition = new Vector2D(goalPosition);
        this.movementDegree = Math.atan((this.goalPosition.x - this.position.x) / (this.goalPosition.y - this.position.y));
        this.movementDegreeSinCos.x = (float)Math.sin(movementDegree);
        this.movementDegreeSinCos.y = (float)Math.cos(movementDegree);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        synchronized (this) {
            Vector2D pos = new Vector2D(this.position);
            canvas.rotate(-(float) (this.movementDegree * 180 / Math.PI), pos.x, pos.y);
            canvas.drawBitmap(this.sprite, pos.x, pos.y, this.paint);
            canvas.rotate((float) (this.movementDegree * 180 / Math.PI), pos.x, pos.y);
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
