package com.cho0148.piratesiege.drawables;


import com.cho0148.piratesiege.Vector2D;

public interface IHittable {
    void takeDamage(int damage);
    boolean isFriendly();
    boolean contains(Vector2D point);
}
