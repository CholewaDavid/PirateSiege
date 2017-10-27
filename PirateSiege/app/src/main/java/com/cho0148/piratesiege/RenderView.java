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

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class RenderView extends SurfaceView implements Runnable {
    private int fps;
    private int frame_period;
    private SurfaceHolder surfaceHolder;
    private Paint clearPaint;
    private List<Drawable> drawables;
    private Thread renderThread = null;
    private volatile boolean running = false;

    public RenderView(int fps, Context context, SurfaceView view){
        super(context);
        this.fps = fps;
        this.surfaceHolder = view.getHolder();

        this.clearPaint = new Paint();
        this.clearPaint.setColor(Color.rgb(170, 170, 220));
        this.clearPaint.setStyle(Paint.Style.FILL);

        this.frame_period = 1000/this.fps;
        this.drawables = new ArrayList<>();
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
            sleep_time = end_time - begin_time - frame_period;
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
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), this.clearPaint);
        for (Drawable d : drawables) {
            d.draw(canvas);
        }
        this.surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void addDrawable(Drawable d){
        this.drawables.add(d);
    }
}
