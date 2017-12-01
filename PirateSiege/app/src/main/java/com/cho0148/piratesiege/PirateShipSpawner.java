package com.cho0148.piratesiege;

import com.cho0148.piratesiege.drawables.DrawableFactory;
import com.cho0148.piratesiege.drawables.PirateShip;
import com.cho0148.piratesiege.drawables.Ship;

import java.util.Random;


public class PirateShipSpawner {
    private long nextShipSpawn;
    private long lastSpawnCooldown;
    private double cooldownModifier;
    private int shipSpawnPosX;
    private int arenaSizeY;
    private int shipSpeed;
    private int shipRange;
    private int shipShotCooldown;
    private int shipHealth;
    private Random random;

    public PirateShipSpawner(long firstSpawnCooldown, double cooldownModifier, int shipSpawnPosX, int arenaSizeY){
        this.lastSpawnCooldown = firstSpawnCooldown;
        this.cooldownModifier = cooldownModifier;
        this.nextShipSpawn = System.currentTimeMillis() + this.lastSpawnCooldown;
        this.shipSpawnPosX = shipSpawnPosX;
        this.arenaSizeY = arenaSizeY;
        this.shipSpeed = 3;
        this.shipRange = 200;
        this.shipShotCooldown = 5000;
        this.shipHealth = 15;
        this.random = new Random();
    }

    public void update(){
        if(nextShipSpawn < System.currentTimeMillis()){
            Vector2D newShipPos = new Vector2D(shipSpawnPosX, random.nextInt(this.arenaSizeY));
            PirateShip newShip = DrawableFactory.createPirateShip(Ship.ShipSpriteVariant.PIRATE, newShipPos, this.shipSpeed, this.shipRange, this.shipShotCooldown, this.shipHealth);
            newShip.setGoalPosition(new Vector2D(Game.getCityPosX(), random.nextInt(this.arenaSizeY)), true);
            if(!(this.lastSpawnCooldown < 500))
                this.lastSpawnCooldown *= this.cooldownModifier;
            this.nextShipSpawn += this.lastSpawnCooldown;
        }
    }
}
