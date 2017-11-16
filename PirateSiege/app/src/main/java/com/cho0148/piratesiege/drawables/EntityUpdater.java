package com.cho0148.piratesiege.drawables;


import android.content.Context;
import android.content.Entity;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class EntityUpdater implements Runnable {
    private int fps;
    private int framePeriod;
    private static volatile List<MyDrawable> updatables;
    private static List<MyDrawable> newUpdatablesBuffer;
    private static List<MyDrawable> destroyedUpdatables;
    private static List<Cannonball> cannonballs;
    private static List<IHittable> hittables;
    private Thread updateThread;
    private volatile boolean running;

    public EntityUpdater(int fps){
        this.fps = fps;
        this.framePeriod = 1000/this.fps;
        updatables = new ArrayList<>();
        newUpdatablesBuffer = new ArrayList<>();
        destroyedUpdatables = new ArrayList<>();
        cannonballs = new ArrayList<>();
        hittables = new ArrayList<>();
    }

    @Override
    public void run() {
        long beginTime;
        long endTime;
        long sleepTime;
        while(this.running) {
            beginTime = System.currentTimeMillis();
            this.clearBuffer();
            this.removeDestroyed();
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
                this.clearBuffer();
                this.removeDestroyed();
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
        synchronized (EntityUpdater.class) {
            for (MyDrawable entity : updatables) {
                if(entity.isDestroyed()){
                    destroyedUpdatables.add(entity);
                    continue;
                }
                entity.update();
            }

            for(Cannonball cannonball : cannonballs){
                for(IHittable hittable : hittables){
                    if(hittable.contains(cannonball.position)){
                        hittable.takeDamage(cannonball.getDamage());
                        cannonball.destroy();
                    }
                }
            }
        }
    }

    public void addUpdatable(MyDrawable d){
        newUpdatablesBuffer.add(d);
    }

    private void clearBuffer(){
        synchronized (EntityUpdater.class) {
            for (MyDrawable entity : newUpdatablesBuffer) {
                updatables.add(entity);
                if(entity instanceof Cannonball)
                    cannonballs.add((Cannonball)entity);
                else if(entity instanceof IHittable)
                    hittables.add((IHittable)entity);
            }
            newUpdatablesBuffer.clear();
        }
    }

    private void removeDestroyed(){
        for(MyDrawable entity : destroyedUpdatables){
            updatables.remove(entity);
            cannonballs.remove(entity);
            hittables.remove(entity);
        }

        destroyedUpdatables.clear();
    }
}
