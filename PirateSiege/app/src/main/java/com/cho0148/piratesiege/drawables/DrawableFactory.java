package com.cho0148.piratesiege.drawables;

import android.graphics.Bitmap;

import com.cho0148.piratesiege.RenderView;
import com.cho0148.piratesiege.Vector2D;

public final class DrawableFactory {
    private static RenderView renderView;

    private DrawableFactory(){

    }

    public static void init(RenderView rv){
        renderView = rv;
    }

    public static MapTile createMapTile(Bitmap sprite, Vector2D position){
        MapTile mapTile = new MapTile(sprite, position);
        renderView.addDrawable(mapTile);
        return mapTile;
    }
}
