package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;

import com.cho0148.piratesiege.Vector2D;


public abstract class HittableEntity extends MyDrawable implements IHittable {
    protected float health;
    protected float maxHealth;

    public HittableEntity(Bitmap sprite, Vector2D position, float health){
        super(sprite, position);
        this.health = health;
        this.maxHealth = health;
    }

    public HittableEntity(float health){
        super();
        this.health = health;
        this.maxHealth = health;
    }

    public void takeDamage(float damage){
        this.health -= damage;
        if(this.health <= 0)
            this.destroy();
    }
}
