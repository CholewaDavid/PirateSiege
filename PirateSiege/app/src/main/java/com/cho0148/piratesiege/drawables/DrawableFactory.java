package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ProgressBar;

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

    public static Ship createShip(Ship.ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown){
        Ship newShip = new Ship(variant, position, speed, range, shotCooldown);
        renderView.addDrawable(newShip);
        entityUpdater.addUpdatable(newShip);
        return newShip;
    }

    public static Cannonball createCannonball(Vector2D position, Vector2D goalPosition, float speed, int damage){
        Bitmap sprite = BitmapFactory.decodeResource(Game.getContext().getResources(), (Game.getContext().getResources().getIdentifier("cannonball", "drawable", "com.cho0148.piratesiege")));
        Cannonball newCannonball = new Cannonball(sprite, position, goalPosition, speed, damage);
        renderView.addDrawable(newCannonball);
        entityUpdater.addUpdatable(newCannonball);
        return newCannonball;
    }
}
