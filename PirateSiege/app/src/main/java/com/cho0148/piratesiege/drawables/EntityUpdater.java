package com.cho0148.piratesiege.drawables;

import com.cho0148.piratesiege.PirateShipSpawner;
import com.cho0148.piratesiege.PlayerShipController;

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
    private static List<HittableEntity> hittables;
    private static PirateShipSpawner pirateShipSpawner = null;
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
            if(Thread.interrupted())
                return;
            beginTime = System.currentTimeMillis();
            this.clearBuffer();
            this.removeDestroyed();
            this.update();
            endTime = System.currentTimeMillis();
            sleepTime = framePeriod - (endTime - beginTime);

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
        updateThread.interrupt();
        /*
        while(true){
            try{
                updateThread.join();
            }catch(InterruptedException e){

            }
        }
        */
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
                for(HittableEntity hittable : hittables){
                    if(!cannonball.isDestroyed() && hittable.isFriendly() != cannonball.isFriendly() && hittable.contains(cannonball.position)){
                        hittable.takeDamage(cannonball.getDamage());
                        cannonball.destroy();
                    }
                }
            }

            if(pirateShipSpawner != null){
                pirateShipSpawner.update();
            }
        }
    }

    public void addUpdatable(MyDrawable d){
        newUpdatablesBuffer.add(d);
    }

    public void setPirateShipSpawner(long firstSpawnCooldown, double cooldownModifier, int shipSpawnPosX, int arenaSizeY){
        pirateShipSpawner = new PirateShipSpawner(firstSpawnCooldown, cooldownModifier, shipSpawnPosX, arenaSizeY);
    }

    public static List<HittableEntity> getHittables(){
        return hittables;
    }

    private void clearBuffer(){
        synchronized (EntityUpdater.class) {
            for (MyDrawable entity : newUpdatablesBuffer) {
                updatables.add(entity);
                if(entity instanceof Cannonball)
                    cannonballs.add((Cannonball)entity);
                else if(entity instanceof HittableEntity)
                    hittables.add((HittableEntity) entity);
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

        PlayerShipController.removeDestroyed();
        destroyedUpdatables.clear();
    }
}
