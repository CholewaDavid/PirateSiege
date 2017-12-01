package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.Vector2D;


public class Wall extends MyDrawable implements IHittable{
    private Cannon cannon = null;
    private boolean showCannonPrice = false;
    private Label cannonPriceLabel;


    public Wall(Bitmap sprite, Vector2D position){
        super(sprite, position);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(30);
        Typeface font = Typeface.createFromAsset(Game.getContext().getAssets(), "fonts/Kenney Mini.ttf");
        textPaint.setTypeface(font);

        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.YELLOW);
        rectPaint.setStyle(Paint.Style.FILL);

        Vector2D labelPos = new Vector2D(this.position.x + this.sprite.getWidth(), this.position.y);
        this.cannonPriceLabel = new Label("", 5, 3, labelPos, textPaint, rectPaint);
    }

    public Vector2D getPosition(){
        return this.position;
    }

    public void buildCannon(){
        if(this.cannon != null){
            this.cannon.upgrade();
            return;
        }

        Vector2D pos = new Vector2D(this.position.x + this.sprite.getWidth()/2, this.position.y + this.sprite.getHeight()/2);
        this.cannon = DrawableFactory.createCannon(pos, 2000, 400);
    }

    public int getCannonPrice(){
        if(this.cannon == null)
            return 100;
        return (this.cannon.getCannonLevel() + 1)* 100;
    }

    public void setShowCannonPrice(boolean showCannonPrice){
        this.showCannonPrice = showCannonPrice;
        if(showCannonPrice)
            this.cannonPriceLabel.setText("" + this.getCannonPrice());
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
            if(this.showCannonPrice){
                this.cannonPriceLabel.draw(canvas);
            }
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
    public void takeDamage(float damage) {

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
