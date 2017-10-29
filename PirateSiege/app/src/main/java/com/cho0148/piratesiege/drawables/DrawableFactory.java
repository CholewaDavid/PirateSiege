package com.cho0148.piratesiege.drawables;

import com.cho0148.piratesiege.RenderView;

public final class DrawableFactory {
    private static RenderView renderView;
    private static EntityUpdater entityUpdater;

    private DrawableFactory(){

    }

    public static void init(RenderView rv, EntityUpdater eu){
        renderView = rv;
        entityUpdater = eu;
    }

    public static MapGrid createMapGrid(){
        MapGrid newMapGrid = new MapGrid();
        renderView.addDrawable(newMapGrid);
        entityUpdater.addUpdatable(newMapGrid);
        return newMapGrid;
    }

    public static City createCity(){
        City newCity = new City();
        renderView.addDrawable(newCity);
        entityUpdater.addUpdatable(newCity);
        return newCity;
    }
}
