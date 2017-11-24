package com.cho0148.piratesiege.drawables;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.Vector2D;

import java.util.List;

public class Cannon extends MyDrawable {
    private long nextShotTime;
    private int shotCooldown;
    private int range;
    private HittableEntity target;
    private int level;

    public Cannon(Bitmap sprite, Vector2D position, int shotCooldown, int range){
        super(sprite, new Vector2D(position.x - sprite.getWidth()/2, position.y - sprite.getHeight()/2));

        this.shotCooldown = shotCooldown;
        this.nextShotTime = System.currentTimeMillis() + shotCooldown;
        this.range = range;
        this.target = null;
        this.level = 1;
    }

    public int getCannonLevel(){
        return this.level;
    }

    @Override
    public void update() {
        synchronized (this){
            if (this.target != null && this.target.isDestroyed()) {
                this.target = null;
                return;
            }

            if(System.currentTimeMillis() > this.nextShotTime) {
                if (this.target == null) {
                    this.lookForTarget();
                    return;
                }
                this.shoot();
            }
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        synchronized (this) {
            canvas.drawBitmap(this.sprite, this.position.x, this.position.y, this.paint);
        }
    }

    public void shoot(){
        DrawableFactory.createCannonball(new Vector2D(this.position), new Vector2D(target.getPosition()), 20, 10, true);
        this.nextShotTime = System.currentTimeMillis() + this.shotCooldown;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    private void lookForTarget(){
        List<HittableEntity> hittables = EntityUpdater.getHittables();
        for(HittableEntity hittable : hittables){
            if(!hittable.isDestroyed() && !hittable.isFriendly() && this.inRange(hittable.getPosition())){
                this.target = hittable;
                return;
            }
        }
    }

    private boolean inRange(Vector2D pos){
        double dx = Math.abs(pos.x - this.position.x);
        double dy = Math.abs(pos.y - this.position.y);

        if(dx > this.range || dy > this.range)
            return false;

        if(dx + dy <= this.range)
            return true;

        if(dx*dx + dy*dy <= this.range * this.range)
            return true;

        return false;
    }
}
