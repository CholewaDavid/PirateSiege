package com.cho0148.piratesiege;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

public class Game extends AppCompatActivity {
    public final int FPS = 60;

    private RenderView renderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        renderView = new RenderView(FPS, this.getBaseContext(), (SurfaceView)findViewById(R.id.surfaceView));
        renderView.resume();

        renderView.addDrawable(new MapTile());
    }
}
