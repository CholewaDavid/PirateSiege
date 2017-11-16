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


public class Wall extends MyDrawable {
    private Bitmap sprite;
    private Vector2D position;
    private Paint paint;

    public Wall(Bitmap sprite, Vector2D position){
        this.sprite = sprite;
        this.position = position;
        this.paint = new Paint();
    }

    public Vector2D getPosition(){
        return this.position;
    }

    @Override
    public void setScale(Vector2D scale) {

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
}
