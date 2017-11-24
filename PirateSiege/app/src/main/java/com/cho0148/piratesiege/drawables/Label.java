package com.cho0148.piratesiege.drawables;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cho0148.piratesiege.Vector2D;


public class Label extends MyDrawable {
    private String text;
    private int paddingX;
    private int paddingY;
    private Rect rect;
    private Paint textPaint;
    private Paint rectPaint;

    public Label(String text, int paddingX, int paddingY, Vector2D position, Paint textPaint, Paint rectPaint){
        this.text = text;
        this.paddingX = paddingX;
        this.paddingY = paddingY;
        this.position = position;
        this.textPaint = textPaint;
        this.rectPaint = rectPaint;

        this.computeDimensions();
    }

    public void setText(String text){
        this.text = text;
        this.computeDimensions();
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(this.rect, this.rectPaint);
        canvas.drawText(this.text, this.position.x, this.position.y, this.textPaint);
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

    private void computeDimensions(){
        Rect textSize = new Rect();
        this.textPaint.getTextBounds(this.text, 0, this.text.length(), textSize);
        this.rect = new Rect((int)(this.position.x - this.paddingX), (int)(this.position.y + this.paddingY), (int)(this.position.x + textSize.width() + this.paddingX), (int)(this.position.y - textSize.height() - this.paddingY));
    }
}
