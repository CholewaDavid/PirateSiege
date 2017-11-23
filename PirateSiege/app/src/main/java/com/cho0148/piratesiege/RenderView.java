package com.cho0148.piratesiege;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.cho0148.piratesiege.drawables.MyDrawable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class RenderView extends SurfaceView implements Runnable {
    private int fps;
    private int frame_period;
    private SurfaceHolder surfaceHolder;
    private Paint clearPaint;
    private static List<MyDrawable> drawables;
    private static List<MyDrawable> newDrawablesBuffer;
    private static List<MyDrawable> destroyedDrawables;
    private Thread renderThread = null;
    private volatile boolean running = false;
    private boolean firstScalingDone = false;
    private Vector2D scale;

    public RenderView(int fps, Context context, SurfaceView view){
        super(context);
        this.fps = fps;
        this.surfaceHolder = view.getHolder();

        this.clearPaint = new Paint();
        this.clearPaint.setColor(Color.rgb(171, 227, 245));
        this.clearPaint.setStyle(Paint.Style.FILL);

        this.frame_period = 1000/this.fps;
        this.scale = new Vector2D();
        drawables = new ArrayList<>();
        newDrawablesBuffer = new ArrayList<>();
        destroyedDrawables = new ArrayList<>();
    }

    @Override
    public void run(){
        long begin_time;
        long end_time;
        long sleep_time;
        while(this.running) {
            if(Thread.interrupted())
                return;
            if(!this.surfaceHolder.getSurface().isValid()){
                continue;
            }
            begin_time = System.currentTimeMillis();
            this.clearBuffer();
            this.removeDestroyed();
            this.draw();
            end_time = System.currentTimeMillis();
            sleep_time = frame_period - (end_time - begin_time);

            if(sleep_time > 0){
                try {
                    sleep(sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(sleep_time < 0){
                this.clearBuffer();
                this.removeDestroyed();
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
        renderThread.interrupt();
        /*
        while(true){
            try{
                renderThread.join();
            }catch(InterruptedException e){

            }
        }
        */
    }

    public void draw(){
        synchronized (RenderView.class) {
            Canvas canvas = this.surfaceHolder.lockCanvas();
            if(canvas == null)
                return;
            if (!this.firstScalingDone) {
                this.computeScale(canvas);

                this.firstScalingDone = true;
            }
            canvas.scale(this.scale.x, this.scale.y);

            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), this.clearPaint);
            for (MyDrawable d : drawables) {
                if(d.isDestroyed()) {
                    destroyedDrawables.add(d);
                    continue;
                }
                d.draw(canvas);
            }
            canvas.save();
            canvas.restore();

            this.surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void addDrawable(MyDrawable d){
        newDrawablesBuffer.add(d);
    }

    public Vector2D getScale(){
        return this.scale;
    }

    private void clearBuffer(){
        synchronized (RenderView.class) {
            for (MyDrawable drawable : newDrawablesBuffer) {
                drawables.add(drawable);
            }
            newDrawablesBuffer.clear();
        }
    }

    private void removeDestroyed(){
        for(MyDrawable drawable : destroyedDrawables){
            drawables.remove(drawable);
        }
        destroyedDrawables.clear();
    }

    private void computeScale(Canvas canvas){
        Vector2D tileAmount = Game.getTileAmount();
        Vector2D canvasSize = new Vector2D(canvas.getWidth(), canvas.getHeight());
        Vector2D tileSize = Game.getTileSize();
        float newTileSizeX = canvasSize.x / tileAmount.x;
        float newTileSizeY = canvasSize.y / tileAmount.y;
        float scale = 0;

        if (newTileSizeX < newTileSizeY) {
            scale = newTileSizeX / tileSize.x;
        } else {
            scale = newTileSizeY / tileSize.y;
        }

        this.scale.x = scale;
        this.scale.y = scale;

        Game.setAreaSize(new Vector2D(canvas.getWidth() / this.scale.x, canvas.getHeight() / this.scale.y));
    }
}
