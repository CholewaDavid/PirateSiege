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

import com.cho0148.piratesiege.Game;
import com.cho0148.piratesiege.R;
import com.cho0148.piratesiege.Vector2D;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class City extends MyDrawable{
    private final int MAX_MORALE = 100;
    private List<Wall> walls;
    private int morale;
    private int money;

    City(){
        this.morale = this.MAX_MORALE;
        this.money = 0;
        this.walls = new ArrayList<>();
        this.loadCity(Game.getContext());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        synchronized (City.class) {
            for (Wall wall : this.walls)
                wall.draw(canvas);
        }
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
    public void setScale(Vector2D scale) {
        for(Wall wall : this.walls){
            wall.setScale(scale);
        }
    }

    @Override
    public void update() {
        synchronized (City.class) {
            for (Wall wall : this.walls) {
                wall.update();
            }
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
}
