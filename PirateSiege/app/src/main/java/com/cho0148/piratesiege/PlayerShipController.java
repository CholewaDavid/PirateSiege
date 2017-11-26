package com.cho0148.piratesiege;


import android.graphics.Rect;

import com.cho0148.piratesiege.drawables.EntityUpdater;
import com.cho0148.piratesiege.drawables.HittableEntity;
import com.cho0148.piratesiege.drawables.IHittable;
import com.cho0148.piratesiege.drawables.PlayerShip;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;

public class PlayerShipController {
    private static List<PlayerShip> playerShips = new ArrayList<>();
    private static boolean selectingShips = false;

    public static void addPlayerShip(PlayerShip ship){
        playerShips.add(ship);
    }

    public static void removeDestroyed(){
        for(PlayerShip ship : playerShips){
            if(ship.isDestroyed())
                playerShips.remove(ship);
        }
    }

    public static void selectShips(Rect area){
        for(PlayerShip ship : playerShips){
            if(area.contains((int)ship.getPosition().x, (int)ship.getPosition().y))
                ship.setSelected(true);
            else
                ship.setSelected(false);
        }
    }

    public static boolean isSelectingShips(){
        return selectingShips;
    }

    public static void setSelectingShips(boolean isSelectingShips){
        selectingShips = isSelectingShips;
    }

    public static void setTarget(Vector2D position){
        if(position.x < Game.getCityPosX()){
            return;
        }
        List<HittableEntity> hittables = EntityUpdater.getHittables();
        HittableEntity foundHittable = null;
        boolean foundTarget = false;
        for(HittableEntity hittable : hittables){
            if(!hittable.isDestroyed() && !hittable.isFriendly() && hittable.contains(position)){
                foundHittable = hittable;
                foundTarget = true;
                break;
            }
        }
        if(foundTarget){
            for(PlayerShip ship : playerShips){
                if(ship.getSelected())
                    ship.setPirateTarget(foundHittable);
            }
        }
        else {
            for (PlayerShip ship : playerShips) {
                if (ship.getSelected()) {
                    ship.setGoalPosition(position, false);
                    ship.setPirateTarget(null);
                }
            }
        }
    }
}
