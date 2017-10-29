package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cho0148.piratesiege.Vector2D;


public class Wall extends MyDrawable {
    private Bitmap sprite;
    private Vector2D position;
    private Vector2D defaultSpriteSize;
    private Vector2D defaultPosition;
    private Paint paint;

    public Wall(Bitmap sprite, Vector2D position){
        this.sprite = sprite;
        this.position = position;
        this.defaultPosition = this.position;

        this.defaultSpriteSize = new Vector2D(this.sprite.getWidth(), this.sprite.getHeight());
        this.paint = new Paint();
    }

    @Override
    public void setScale(Vector2D scale) {
        this.sprite = Bitmap.createScaledBitmap(this.sprite, (int)(Math.ceil(this.defaultSpriteSize.x * scale.x)), (int)(Math.ceil(this.defaultSpriteSize.y * scale.y)), true);
        this.position.x = (float)(Math.floor(this.defaultPosition.x * scale.x));
        this.position.y = (float)(Math.floor(this.defaultPosition.y * scale.y));
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(this.sprite, this.position.x, this.position.y, this.paint);
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
