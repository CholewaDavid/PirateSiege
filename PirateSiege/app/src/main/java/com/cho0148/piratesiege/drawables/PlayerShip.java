package com.cho0148.piratesiege.drawables;


import com.cho0148.piratesiege.Vector2D;

public class PlayerShip extends Ship {
    private boolean leftPort;

    public PlayerShip(ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown, int health) {
        super(variant, position, speed, range, shotCooldown, health);
        this.leftPort = false;
    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public void setGoalPosition(Vector2D position, boolean goalPositionShoot){
        if(!leftPort) {
            if(this.goalPosition == null)
                super.setGoalPosition(position, false);
        }
        else
            super.setGoalPosition(position, goalPositionShoot);
    }
}
