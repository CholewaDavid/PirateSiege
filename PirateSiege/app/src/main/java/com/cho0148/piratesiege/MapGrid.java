package com.cho0148.piratesiege;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cho0148.piratesiege.drawables.DrawableFactory;
import com.cho0148.piratesiege.drawables.MapTile;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapGrid {
    private List<List<MapTile>> tiles;
    private Game game;
    private Vector2D tileAmount;
    private Vector2D tileSize;

    MapGrid(Game game) {
        this.game = game;
        this.tiles = new ArrayList<List<MapTile>>();
        this.tileAmount = new Vector2D();
        this.tileSize = new Vector2D();
        this.loadMap(this.game.getBaseContext());
    }

    public Vector2D getTileAmount(){
        return this.tileAmount;
    }

    public Vector2D getTileSize(){
        return this.tileSize;
    }

    private void loadMap(Context context) {
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.map_description);

            int current_row = -1;
            int current_column = -1;
            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                String name = parser.getName();
                switch(event){
                    case XmlPullParser.START_TAG: {
                        if(name.equals("map")){
                            this.tileAmount.x = Integer.parseInt(parser.getAttributeValue(null, "columns"));
                            this.tileAmount.y = Integer.parseInt(parser.getAttributeValue(null, "rows"));
                            int tileSize = Integer.parseInt(parser.getAttributeValue(null, "tileSize"));
                            this.tileSize.x = tileSize;
                            this.tileSize.y = tileSize;
                        }
                        else if (name.equals("row")) {
                            current_row++;
                            current_column = -1;
                            this.tiles.add(new ArrayList<MapTile>());
                        } else if (name.equals("tile")) {
                            current_column++;
                            String ground_tile_name = parser.getAttributeValue(null, "ground");
                            int id = this.game.getBaseContext().getResources().getIdentifier(ground_tile_name, "drawable", "com.cho0148.piratesiege");
                            Bitmap ground_bitmap = BitmapFactory.decodeResource(this.game.getBaseContext().getResources(), id);
                            this.tiles.get(current_row).add(DrawableFactory.createMapTile(ground_bitmap, new Vector2D(current_column, current_row)));
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
