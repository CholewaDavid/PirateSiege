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
    public static enum MapTileType{LAND, WATER}

    private static double waterOffsetStep;
    private static double waterOffset;
    private Bitmap undergroundSprite;
    private Vector2D tilePosition;
    private boolean outOfBounds;
    private MapTileType type;

    MapTile(Bitmap sprite, Vector2D tilePosition, MapTileType type){
        super(sprite, computePosition(tilePosition, sprite));
        this.tilePosition = tilePosition;
        this.type = type;
        this.outOfBounds = false;
        this.undergroundSprite = null;
    }

    public static void updateStatic(){
        waterOffsetStep+=0.05;
        waterOffset = Math.sin(waterOffsetStep);
    }

    public void setOutOfBounds(boolean outOfBounds){
        this.outOfBounds = outOfBounds;
    }

    public void setUndergroundSprite(Bitmap sprite){
        this.undergroundSprite = sprite;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(this.type == MapTileType.WATER)
            canvas.drawBitmap(this.sprite, this.position.x + (int)(waterOffset*10), this.position.y, this.paint);
        else
            canvas.drawBitmap(this.sprite, this.position.x, this.position.y, this.paint);
    }

    public void drawUnderground(Canvas canvas){
        if(this.undergroundSprite != null)
            canvas.drawBitmap(this.undergroundSprite, this.position.x + (int)(waterOffset*10), this.position.y, this.paint);
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

    private static Vector2D computePosition(Vector2D tilePosition, Bitmap sprite){
        return new Vector2D(tilePosition.x * sprite.getWidth(), tilePosition.y * sprite.getHeight());
    }
}
