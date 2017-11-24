package com.cho0148.piratesiege.drawables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.R;
import com.cho0148.piratesiege.Vector2D;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class City extends HittableEntity{
    private List<Wall> walls;
    private int money;
    private ProgressBar progressBarMorale;
    private boolean buildingCannon;
    private double healthRegen;

    City(ProgressBar progressBar){
        super(100);
        this.money = 100;
        this.walls = new ArrayList<>();
        this.progressBarMorale = progressBar;
        this.buildingCannon = false;
        this.healthRegen = 0.005;
        this.loadCity(Game.getContext());
    }

    public void handleClick(Vector2D position){
        if(this.buildingCannon){
            for(Wall wall : this.walls){
                if(wall.contains(position)){
                    wall.buildCannon();
                    break;
                }
            }
            this.buildingCannon = false;
        }
    }

    public void setBuildingCannon(boolean building){
        this.buildingCannon = building;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        synchronized (City.class) {
            for (Wall wall : this.walls)
                wall.draw(canvas);
        }
    }

    public float getPositionX(){
        return this.walls.get(0).getPosition().x;
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void update() {
        synchronized (City.class) {
            for (Wall wall : this.walls) {
                wall.update();
            }
            this.health += this.healthRegen;
            if(this.health > this.maxHealth)
                this.health = this.maxHealth;
            this.progressBarMorale.setProgress((int)(100*this.health/this.maxHealth));
        }
    }

    private void loadCity(Context context){
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.city_description);

            int currentRow = -1;
            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                String name = parser.getName();
                switch(event){
                    case XmlPullParser.START_TAG: {
                        if(name.equals("row")){
                            currentRow++;
                        } else if (name.equals("wall")) {
                            int currentColumn = Integer.parseInt(parser.getAttributeValue(null, "column"));
                            String spriteName = parser.getAttributeValue(null, "sprite");
                            int spriteID = Game.getContext().getResources().getIdentifier(spriteName, "drawable", "com.cho0148.piratesiege");
                            Bitmap sprite = BitmapFactory.decodeResource(Game.getContext().getResources(), spriteID);
                            Vector2D position = new Vector2D(currentColumn, currentRow);
                            position.x *= sprite.getWidth();
                            position.y *= sprite.getHeight();
                            this.walls.add(new Wall(sprite, position));
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        break;
                    }
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public boolean contains(Vector2D point) {
        for(Wall w : this.walls){
            if(w.contains(point))
                return true;
        }
        return false;
    }

    @Override
    protected void destroy(){

    }
}
