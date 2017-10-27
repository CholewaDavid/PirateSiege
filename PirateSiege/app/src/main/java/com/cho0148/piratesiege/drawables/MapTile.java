package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cho0148.piratesiege.Vector2D;


public class MapTile extends Drawable {
    private Bitmap sprite;
    private Vector2D position;
    private Vector2D tile_position;
    private Paint paint;

    MapTile(Bitmap sprite, Vector2D tile_position){
        this.sprite = sprite;
        this.tile_position = tile_position;

        this.position = new Vector2D(this.tile_position.x * this.sprite.getWidth(), this.tile_position.y * this.sprite.getHeight());
        this.paint = new Paint();
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
