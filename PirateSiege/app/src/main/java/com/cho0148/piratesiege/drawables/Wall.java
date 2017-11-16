package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cho0148.piratesiege.Vector2D;


public class Wall extends MyDrawable implements IHittable{
    public Wall(Bitmap sprite, Vector2D position){
        super(sprite, position);
    }

    public Vector2D getPosition(){
        return this.position;
    }

    @Override
    public void update() {
        synchronized (this){

        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        synchronized (this) {
            canvas.drawBitmap(this.sprite, this.position.x, this.position.y, this.paint);
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

    @Override
    public void takeDamage(int damage) {

    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public boolean contains(Vector2D point) {
        return point.x > this.position.x && point.y > this.position.y && point.x < this.position.x + this.sprite.getWidth() && point.y < this.position.y + this.sprite.getHeight();
    }
}
