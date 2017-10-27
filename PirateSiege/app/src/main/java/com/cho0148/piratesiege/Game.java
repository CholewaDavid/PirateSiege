package com.cho0148.piratesiege;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import com.cho0148.piratesiege.drawables.DrawableFactory;

public class Game extends AppCompatActivity {
    public final int FPS = 60;

    private RenderView renderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        renderView = new RenderView(FPS, this.getBaseContext(), (SurfaceView)findViewById(R.id.surfaceView));
        DrawableFactory.init(renderView);

        renderView.resume();
        MapGrid map_grid = new MapGrid(this);
    }
}
