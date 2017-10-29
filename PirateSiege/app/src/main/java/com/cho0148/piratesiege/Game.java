package com.cho0148.piratesiege;


import android.content.Context;
import android.content.Entity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.cho0148.piratesiege.drawables.City;
import com.cho0148.piratesiege.drawables.DrawableFactory;
import com.cho0148.piratesiege.drawables.EntityUpdater;
import com.cho0148.piratesiege.drawables.MapGrid;

public final class Game extends AppCompatActivity {
    public final int FPS = 60;

    private static RenderView renderView;
    private static EntityUpdater entityUpdater;
    private static MapGrid mapGrid;
    private static City city;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        context = this.getBaseContext();

        renderView = new RenderView(FPS, context, (SurfaceView)findViewById(R.id.surfaceView));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = displayMetrics.heightPixels;
        renderView.setLayoutParams(layoutParams);

        entityUpdater = new EntityUpdater(FPS);

        DrawableFactory.init(renderView, entityUpdater);

        mapGrid = DrawableFactory.createMapGrid();
        city = DrawableFactory.createCity();
        renderView.resume();
        entityUpdater.resume();
    }

    public static Vector2D getRenderViewSize(){
        return new Vector2D(renderView.getWidth(), renderView.getHeight());
    }

    public static Vector2D getRenderViewScale(){
        return renderView.getScale();
    }

    public static Vector2D getTileAmount(){
        return mapGrid.getTileAmount();
    }

    public static Vector2D getTileSize(){
        return mapGrid.getTileSize();
    }

    public static Context getContext(){
        return context;
    }
}
