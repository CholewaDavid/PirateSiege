package com.cho0148.piratesiege.drawables;

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.Vector2D;


public class PirateShip extends Ship {
    public PirateShip(ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown, int health){
        super(variant, position, speed, range, shotCooldown, health);
    }

    @Override
    public boolean isFriendly() {
        return false;
    }

    @Override
    protected void destroy(){
        super.destroy();
        Game.addMoneyToCity(20);
    }
}
