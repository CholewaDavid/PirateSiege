package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.cho0148.piratesiege.Vector2D;


public abstract class MyDrawable extends Drawable {
    protected boolean destroyed = false;
    protected Bitmap sprite;
    protected Vector2D position;
    protected Paint paint;

    public abstract void update();

    public MyDrawable(Bitmap sprite, Vector2D position){
        this.sprite = sprite;
        this.position = position;
        this.paint = new Paint();
    }

    public MyDrawable(){
        this.sprite = null;
        this.position = null;
        this.paint = new Paint();
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    protected void destroy(){
        this.destroyed = true;
    }

    public Vector2D getPosition(){
        return new Vector2D(this.position);
    }
}
