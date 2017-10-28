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


public class MapTile extends MyDrawable {
    private Bitmap sprite;
    private Vector2D position;
    private Vector2D tilePosition;
    private Vector2D defaultSpriteSize;
    private Paint paint;

    MapTile(Bitmap sprite, Vector2D tilePosition){
        this.sprite = sprite;
        this.tilePosition = tilePosition;

        this.sprite = Bitmap.createScaledBitmap(this.sprite, this.sprite.getWidth(), this.sprite.getHeight(), true);
        this.defaultSpriteSize = new Vector2D(this.sprite.getWidth(), this.sprite.getHeight());

        this.setPosition();
        this.paint = new Paint();
    }

    @Override
    public void setScale(Vector2D scale){
        this.sprite = Bitmap.createScaledBitmap(this.sprite, (int)(this.defaultSpriteSize.x * scale.x), (int)(this.defaultSpriteSize.y * scale.y), true);
        this.setPosition();
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

    private void setPosition(){
        this.position = new Vector2D(this.tilePosition.x * this.sprite.getWidth(), this.tilePosition.y * this.sprite.getHeight());
    }
}
