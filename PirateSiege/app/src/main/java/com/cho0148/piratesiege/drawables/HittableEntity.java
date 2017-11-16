package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;

import com.cho0148.piratesiege.Vector2D;


public abstract class HittableEntity extends MyDrawable implements IHittable {
    protected int health;
    protected int maxHealth;

    public HittableEntity(Bitmap sprite, Vector2D position, int health){
        super(sprite, position);
        this.health = health;
        this.maxHealth = health;
    }

    public HittableEntity(int health){
        super();
        this.health = health;
        this.maxHealth = health;
    }

    public void takeDamage(int damage){
        this.health -= damage;
        if(this.health <= 0)
            this.destroy();
    }
}
