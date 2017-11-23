package com.cho0148.piratesiege;


import android.content.Context;
import android.content.Entity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cho0148.piratesiege.drawables.City;
import com.cho0148.piratesiege.drawables.DrawableFactory;
import com.cho0148.piratesiege.drawables.EntityUpdater;
import com.cho0148.piratesiege.drawables.MapGrid;
import com.cho0148.piratesiege.drawables.Ship;

import java.util.Random;

public final class Game extends AppCompatActivity {
    public final int FPS = 60;

    private static RenderView renderView;
    private static EntityUpdater entityUpdater;
    private static MapGrid mapGrid;
    private static City city;
    private static Context context;
    private static Vector2D areaSize = null;

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
        renderView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT);
                return true;
            }
        });
        renderView.resume();

        entityUpdater = new EntityUpdater(FPS);
        entityUpdater.setPirateShipSpawner(5000, 0.95, 2000, 600);
        entityUpdater.resume();

        DrawableFactory.init(renderView, entityUpdater);

        mapGrid = DrawableFactory.createMapGrid();
        city = DrawableFactory.createCity((ProgressBar)(findViewById(R.id.progressBarCityMorale)));

        Button button = (Button)findViewById(R.id.buttonCannon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city.setBuildingCannon(true);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        renderView.pause();
        entityUpdater.pause();

    }

    public static void handleClick(Vector2D position){
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT);
        city.handleClick(position);
    }

    public static Vector2D getRenderViewSize(){
        return new Vector2D(renderView.getWidth(), renderView.getHeight());
    }

    public static Vector2D getCanvasScale(){
        return new Vector2D(renderView.getScale());
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

    public static Vector2D getAreaSize(){
        return areaSize;
    }

    public static void setAreaSize(Vector2D newAreaSize){
        areaSize = newAreaSize;
    }

    public static float getCityPosX(){return city.getPositionX();}
}
