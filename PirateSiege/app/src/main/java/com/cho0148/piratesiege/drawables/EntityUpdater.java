package com.cho0148.piratesiege.drawables;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class EntityUpdater implements Runnable {
    private int fps;
    private int framePeriod;
    private List<MyDrawable> updatables;
    private Thread updateThread;
    private volatile boolean running;

    public EntityUpdater(int fps){
        this.fps = fps;
        this.framePeriod = 1000/this.fps;
        this.updatables = new ArrayList<MyDrawable>();
    }

    @Override
    public void run() {
        long beginTime;
        long endTime;
        long sleepTime;
        while(this.running) {
            beginTime = System.currentTimeMillis();
            this.update();
            endTime = System.currentTimeMillis();
            sleepTime = framePeriod - (beginTime - endTime);
            if(sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(sleepTime < 0){
                this.update();
                sleepTime += this.framePeriod;
            }
        }
    }

    public void resume(){
        this.running = true;
        this.updateThread = new Thread(this);
        this.updateThread.start();
    }

    public void pause(){
        this.running = false;
        while(true){
            try{
                updateThread.join();
            }catch(InterruptedException e){

            }
        }
    }

    public void update(){
        for(MyDrawable entity : this.updatables){
            entity.update();
        }
    }

    public void addUpdatable(MyDrawable d){
        this.updatables.add(d);
    }
}
