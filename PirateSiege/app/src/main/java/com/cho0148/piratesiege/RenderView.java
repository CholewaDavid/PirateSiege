package com.cho0148.piratesiege;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cho0148.piratesiege.drawables.MyDrawable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class RenderView extends SurfaceView implements Runnable {
    private int fps;
    private int frame_period;
    private SurfaceHolder surfaceHolder;
    private Paint clearPaint;
    private List<MyDrawable> drawables;
    private Thread renderThread = null;
    private volatile boolean running = false;
    private boolean firstScalingDone = false;
    private Vector2D scale;

    public RenderView(int fps, Context context, SurfaceView view){
        super(context);
        this.fps = fps;
        this.surfaceHolder = view.getHolder();

        this.clearPaint = new Paint();
        this.clearPaint.setColor(Color.BLUE);
        this.clearPaint.setStyle(Paint.Style.FILL);

        this.frame_period = 1000/this.fps;
        this.scale = new Vector2D();
        this.drawables = new ArrayList<MyDrawable>();
    }

    public void run(){
        long begin_time;
        long end_time;
        long sleep_time;
        while(this.running) {
            if(!this.surfaceHolder.getSurface().isValid()){
                continue;
            }
            begin_time = System.currentTimeMillis();
            this.draw();
            end_time = System.currentTimeMillis();
            sleep_time = frame_period - (begin_time - end_time);
            if(sleep_time > 0){
                try {
                    sleep(sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(sleep_time < 0){
                this.draw();
                sleep_time += this.frame_period;
            }
        }
    }

    public void resume(){
        this.running = true;
        this.renderThread = new Thread(this);
        this.renderThread.start();
    }

    public void pause(){
        this.running = false;
        while(true){
            try{
                renderThread.join();
            }catch(InterruptedException e){

            }
        }
    }

    public void draw(){
        Canvas canvas = this.surfaceHolder.lockCanvas();
        if(!this.firstScalingDone){
            Vector2D tileAmount = Game.getTileAmount();
            Vector2D canvasSize = new Vector2D(canvas.getWidth(), canvas.getHeight());
            Vector2D tileSize = Game.getTileSize();
            float newTileSizeX = canvasSize.x / tileAmount.x;
            float newTileSizeY = canvasSize.y / tileAmount.y;
            float scale = 0;

            if(newTileSizeX < newTileSizeY) {
                scale = newTileSizeX / tileSize.x;
            }
            else {
                scale = newTileSizeY / tileSize.y;
            }

            this.scale.x = scale;
            this.scale.y = scale;

            for(MyDrawable d : this.drawables){
                this.scaleDrawable(d);
            }

            this.firstScalingDone = true;
        }
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), this.clearPaint);
        for (MyDrawable d : drawables) {
            d.draw(canvas);
        }
        this.surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void addDrawable(MyDrawable d){
        this.drawables.add(d);
        if(firstScalingDone){
            d.setScale(this.scale);
        }
    }

    public void scaleDrawable(MyDrawable d){
        d.setScale(this.scale);
    }

    public Vector2D getScale(){
        return this.scale;
    }
}
