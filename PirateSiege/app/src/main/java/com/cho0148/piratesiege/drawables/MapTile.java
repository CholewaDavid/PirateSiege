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


public class MapTile extends MyDrawable {
    private Bitmap sprite;
    private Bitmap undergroundSprite;
    private Vector2D position;
    private Vector2D tilePosition;
    private Vector2D defaultSpriteSize;
    private Vector2D defaultPosition;
    private Paint paint;
    private boolean outOfBounds;
    private float waterOffsetStep;

    MapTile(Bitmap sprite, Vector2D tilePosition){
        this.sprite = sprite;
        this.tilePosition = tilePosition;
        this.outOfBounds = false;
        this.undergroundSprite = null;

        this.setPosition();

        this.defaultPosition = new Vector2D(this.position);
        this.defaultSpriteSize = new Vector2D(this.sprite.getWidth(), this.sprite.getHeight());

        this.paint = new Paint();
    }

    public void setOutOfBounds(boolean outOfBounds){
        this.outOfBounds = outOfBounds;
    }

    public void setUndergroundSprite(Bitmap sprite){
        this.undergroundSprite = sprite;
    }

    @Override
    public void setScale(Vector2D scale){
        if(this.undergroundSprite != null)
            this.undergroundSprite = Bitmap.createScaledBitmap(this.undergroundSprite, (int)(this.defaultSpriteSize.x * scale.x), (int)(this.defaultSpriteSize.y * scale.y), true);
        this.sprite = Bitmap.createScaledBitmap(this.sprite, (int)(this.defaultSpriteSize.x * scale.x), (int)(this.defaultSpriteSize.y * scale.y), true);
        this.setPosition();
    }

    @Override
    public void update() {
        this.waterOffsetStep+=0.1;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(this.undergroundSprite != null)
            canvas.drawBitmap(this.undergroundSprite, this.position.x, this.position.y, this.paint);
        canvas.drawBitmap(this.sprite, this.position.x + (int)(Math.sin(this.waterOffsetStep)*10), this.position.y, this.paint);
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
