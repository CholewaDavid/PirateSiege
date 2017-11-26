package com.cho0148.piratesiege;


import android.graphics.Rect;

import com.cho0148.piratesiege.drawables.HittableEntity;
import com.cho0148.piratesiege.drawables.PlayerShip;

import java.util.ArrayList;
import java.util.List;

public class PlayerShipController {
    private static List<PlayerShip> playerShips = new ArrayList<>();
    private static boolean selectingShips = false;
    private static HittableEntity targetEntity;
    private static Vector2D targetPosition;

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
        targetPosition = position;
        for(PlayerShip ship : playerShips){
            if(ship.getSelected())
                ship.setGoalPosition(position, false);
        }
    }
}
