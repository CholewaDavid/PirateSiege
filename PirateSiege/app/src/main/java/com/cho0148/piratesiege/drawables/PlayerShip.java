package com.cho0148.piratesiege.drawables;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.cho0148.piratesiege.Vector2D;

public class PlayerShip extends Ship {
    private boolean leftPort;
    private boolean selected;
    private Paint selectedPaint;
    private HittableEntity pirateTarget = null;

    public PlayerShip(ShipSpriteVariant variant, Vector2D position, float speed, float range, int shotCooldown, int health) {
        super(variant, position, speed, range, shotCooldown, health);
        this.leftPort = false;
        this.selectedPaint = new Paint();
        ColorFilter colorFilter = new LightingColorFilter(Color.YELLOW, 3);
        this.selectedPaint.setColorFilter(colorFilter);
    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public void draw(@NonNull Canvas canvas){
        synchronized (this) {
            super.draw(canvas);
            if (this.selected){
                Vector2D pos = new Vector2D(this.position);
                canvas.rotate((float) (this.movementDegree * 180 / Math.PI),pos.x + this.sprite.getWidth()/2, pos.y + this.sprite.getHeight()/2);
                canvas.drawBitmap(this.sprite, pos.x, pos.y, this.selectedPaint);
                canvas.rotate(-(float) (this.movementDegree * 180 / Math.PI), pos.x + this.sprite.getWidth()/2, pos.y + this.sprite.getHeight()/2);
            }
        }
    }

    @Override
    public void update() {
        synchronized (this){
            if(this.pirateTarget != null) {
                if(this.pirateTarget.isDestroyed()) {
                    this.setGoalPosition(this.pirateTarget.getPosition(), false);
                    this.pirateTarget = null;
                    this.goalPosition = null;
                }
                else
                    this.setGoalPosition(this.pirateTarget.getPosition(), true);
            }
            super.update();
            if (!this.leftPort && this.goalPosition == null)
                this.leftPort = true;
        }
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public boolean getSelected(){
        return this.selected;
    }

    public void setPirateTarget(HittableEntity target){
        if(!leftPort)
            return;
        this.pirateTarget = target;
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
