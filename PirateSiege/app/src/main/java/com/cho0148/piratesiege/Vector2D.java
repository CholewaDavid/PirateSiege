package com.cho0148.piratesiege;


public class Vector2D {
    public float x;
    public float y;

    public Vector2D(){
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(double x, double y){
        this.x = (float)x;
        this.y = (float)y;
    }

    public Vector2D(Vector2D vector){
        this.x = vector.x;
        this.y = vector.y;
    }

    @Override
    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
}
