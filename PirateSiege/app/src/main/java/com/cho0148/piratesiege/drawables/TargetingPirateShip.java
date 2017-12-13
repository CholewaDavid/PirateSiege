package com.cho0148.piratesiege.drawables;


import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.PlayerShipController;
import com.cho0148.piratesiege.Vector2D;

import java.util.List;
import java.util.Random;

public class TargetingPirateShip extends Ship {
    private HittableEntity targetShip = null;
    public TargetingPirateShip(ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown, int health) {
        super(variant, position, speed, range, shotCooldown, health);
    }

    @Override
    public void update(){
        super.update();
        if(this.targetShip == null){
            this.goalPosition = null;
            List<PlayerShip> playerShips = PlayerShipController.getPlayerShips();
            if(playerShips.size() == 0)
                return;
            Random r = new Random();
            this.targetShip = playerShips.get(r.nextInt(playerShips.size()));
        }
        if(this.targetShip == null)
            return;

        this.setGoalPosition(this.targetShip.getPosition(), true);
    }

    @Override
    public boolean isFriendly() {
        return false;
    }

    @Override
    protected void destroy(){
        super.destroy();
        Game.addMoneyToCity(30);
    }
}
