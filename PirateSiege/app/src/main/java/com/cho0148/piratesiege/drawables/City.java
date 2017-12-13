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
import android.widget.TextView;

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.R;
import com.cho0148.piratesiege.Vector2D;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class City extends HittableEntity{
    private List<Wall> walls;
    private int money;
    private int highScore;
    private ProgressBar progressBarMorale;
    private boolean buildingCannon;
    private double healthRegen;
    private Vector2D portPosition;
    private Vector2D portGoalPosition;
    private int newShipPrice;

    City(ProgressBar progressBar){
        super(50);
        this.money = 200;
        this.walls = new ArrayList<>();
        this.progressBarMorale = progressBar;
        this.buildingCannon = false;
        this.healthRegen = 0.005;
        this.newShipPrice = 50;
        this.highScore = 0;
        this.loadCity(Game.getContext());
    }

    public void handleClick(Vector2D position){
        if(this.buildingCannon){
            for(Wall wall : this.walls){
                if(wall.contains(position)){
                    int cannonPrice = wall.getCannonPrice();
                    if(cannonPrice <= this.money) {
                        wall.buildCannon();
                        Game.addMoneyToCity(-cannonPrice);
                    }
                    break;
                }
            }
            this.setBuildingCannon(false);
        }
    }

    public void buyNewShip(){
        if(this.buildingCannon)
            return;

        if(this.money < this.newShipPrice)
            return;

        PlayerShip newPlayerShip = DrawableFactory.createPlayerShip(Ship.ShipSpriteVariant.CRUSADER, this.getPortPosition(), 3, 400, 3000, 100);
        newPlayerShip.setGoalPosition(this.getPortGoalPosition(), false);
        Game.addMoneyToCity(-this.newShipPrice);
    }

    public void addMoney(int amount){
        this.money += amount;
        if(amount > 0)
            this.highScore += amount;
    }

    public int getMoney(){
        return this.money;
    }

    public int getHighScore(){
        return this.highScore;
    }

    public Vector2D getPortPosition(){
        return new Vector2D(this.portPosition);
    }

    public Vector2D getPortGoalPosition(){
        return new Vector2D(this.portGoalPosition);
    }

    public boolean getBuildingCannon(){
        return this.buildingCannon;
    }

    public void setBuildingCannon(boolean building){
        this.buildingCannon = building;
        for(Wall wall : this.walls){
            wall.setShowCannonPrice(this.buildingCannon);
        }
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

    public float getPositionXOutside(){return this.walls.get(0).getPosition().x + MapTile.getSpriteSize().x;}

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
            if(this.health <= 0){
                Game.endGame();

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
                        else if(name.equals("port")){
                            int currentColumn = Integer.parseInt(parser.getAttributeValue(null, "column"));
                            Vector2D tileSize = MapTile.getSpriteSize();
                            this.portPosition = new Vector2D(tileSize.x * currentColumn + tileSize.x/2, tileSize.y * currentRow + tileSize.y / 2);
                        }
                        else if(name.equals("portgoal")){
                            int currentColumn = Integer.parseInt(parser.getAttributeValue(null, "column"));
                            Vector2D tileSize = MapTile.getSpriteSize();
                            this.portGoalPosition = new Vector2D(tileSize.x * currentColumn + tileSize.x/2, tileSize.y * currentRow + tileSize.y / 2);
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
