package com.cho0148.piratesiege.drawables;

import com.cho0148.piratesiege.RenderView;

public final class DrawableFactory {
    private static RenderView renderView;

    private DrawableFactory(){

    }

    public static void init(RenderView rv){
        renderView = rv;
    }

    public static MapGrid createMapGrid(){
        MapGrid newMapGrid = new MapGrid();
        renderView.addDrawable(newMapGrid);
        return newMapGrid;
    }

    public static City createCity(){
        City newCity = new City();
        renderView.addDrawable(newCity);
        return newCity;
    }
}
