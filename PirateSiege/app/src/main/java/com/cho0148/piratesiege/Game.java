package com.cho0148.piratesiege;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.cho0148.piratesiege.drawables.DrawableFactory;

public final class Game extends AppCompatActivity {
    public final int FPS = 60;

    private static RenderView renderView;
    private static MapGrid mapGrid;
    private static City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        renderView = new RenderView(FPS, this.getBaseContext(), (SurfaceView)findViewById(R.id.surfaceView));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = displayMetrics.heightPixels;
        renderView.setLayoutParams(layoutParams);

        DrawableFactory.init(renderView);

        mapGrid = new MapGrid();
        city = new City();
        renderView.resume();
    }

    public static Vector2D getRenderViewSize(){
        return new Vector2D(renderView.getWidth(), renderView.getHeight());
    }

    public static Vector2D getTileAmount(){
        return mapGrid.getTileAmount();
    }

    public static Vector2D getTileSize(){
        return mapGrid.getTileSize();
    }
}
