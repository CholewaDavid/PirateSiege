package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.RenderView;
import com.cho0148.piratesiege.Vector2D;

public final class DrawableFactory {
    private static RenderView renderView;
    private static EntityUpdater entityUpdater;

    private DrawableFactory(){

    }

    public static void init(RenderView rv, EntityUpdater eu){
        renderView = rv;
        entityUpdater = eu;
    }

    public static MapGrid createMapGrid(){
        MapGrid newMapGrid = new MapGrid();
        renderView.addDrawable(newMapGrid);
        entityUpdater.addUpdatable(newMapGrid);
        return newMapGrid;
    }

    public static City createCity(ProgressBar progressBar){
        City newCity = new City(progressBar);
        renderView.addDrawable(newCity);
        entityUpdater.addUpdatable(newCity);
        return newCity;
    }

    public static PirateShip createPirateShip(Ship.ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown, int health){
        PirateShip newPirateShip = new PirateShip(variant, position, speed, range, shotCooldown, health);
        renderView.addDrawable(newPirateShip);
        entityUpdater.addUpdatable(newPirateShip);
        return newPirateShip;
    }

    public static PlayerShip createPlayerShip(Ship.ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown, int health){
        PlayerShip newPlayerShip = new PlayerShip(variant, position, speed, range, shotCooldown, health);
        renderView.addDrawable(newPlayerShip);
        entityUpdater.addUpdatable(newPlayerShip);
        return newPlayerShip;
    }

    public static Cannonball createCannonball(Vector2D position, Vector2D goalPosition, float speed, int damage, boolean friendly){
        Bitmap sprite = BitmapFactory.decodeResource(Game.getContext().getResources(), (Game.getContext().getResources().getIdentifier("cannonball", "drawable", "com.cho0148.piratesiege")));
        Cannonball newCannonball = new Cannonball(sprite, position, goalPosition, speed, damage, friendly);
        renderView.addDrawable(newCannonball);
        entityUpdater.addUpdatable(newCannonball);
        return newCannonball;
    }

    public static Cannon createCannon(Vector2D position, int shotCooldown, int range){
        Bitmap sprite = BitmapFactory.decodeResource(Game.getContext().getResources(), (Game.getContext().getResources().getIdentifier("cannon", "drawable", "com.cho0148.piratesiege")));
        Cannon newCannon = new Cannon(sprite, position, shotCooldown, range);
        renderView.addDrawable(newCannon);
        entityUpdater.addUpdatable(newCannon);
        return newCannon;
    }
}
