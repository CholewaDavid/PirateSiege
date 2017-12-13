package com.cho0148.piratesiege;


import android.content.Context;
import android.content.Entity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cho0148.piratesiege.drawables.City;
import com.cho0148.piratesiege.drawables.DrawableFactory;
import com.cho0148.piratesiege.drawables.EntityUpdater;
import com.cho0148.piratesiege.drawables.MapGrid;
import com.cho0148.piratesiege.drawables.Ship;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public final class Game extends AppCompatActivity {
    public final int FPS = 60;

    private static RenderView renderView;
    private static EntityUpdater entityUpdater;
    private static MapGrid mapGrid;
    private static City city;
    private static Context context;
    private static Vector2D areaSize = null;
    private static Game game;
    private static Vector2D clickPosition = null;
    private static LinearLayout layoutEndGame;
    private static TextView textViewScore;
    private static MediaPlayer mainMusic;
    private static boolean playingSounds;
    public static final String HIGHSCORE_URL = "http://homel.vsb.cz/~cho0148/tamzHighscore.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        context = this.getBaseContext();
        game = this;
        mainMusic = MediaPlayer.create(Game.getContext(), R.raw.professor_umlaut);

        renderView = new RenderView(FPS, context, (SurfaceView)findViewById(R.id.surfaceView));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = displayMetrics.heightPixels;
        renderView.setLayoutParams(layoutParams);

        layoutEndGame = (LinearLayout)findViewById(R.id.linearLayoutEndGame);
        textViewScore = (TextView) findViewById(R.id.textViewScore);

        SurfaceView view = (SurfaceView) findViewById(R.id.surfaceView);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Vector2D clickPos = new Vector2D(event.getX() / getCanvasScale().x, event.getY() / getCanvasScale().y);
                if(city.getBuildingCannon())
                    city.handleClick(clickPos);
                else{
                    if(PlayerShipController.isSelectingShips()) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                clickPosition = clickPos;
                                break;
                            case MotionEvent.ACTION_UP:
                                int bottom = (int)Math.max(clickPosition.y, clickPos.y);
                                int top = (int)Math.min(clickPosition.y, clickPos.y);
                                int right = (int)Math.max(clickPosition.x, clickPos.x);
                                int left = (int)Math.min(clickPosition.x, clickPos.x);
                                Rect area = new Rect(left, top, right, bottom);
                                PlayerShipController.selectShips(area);
                                PlayerShipController.setSelectingShips(false);
                        }
                    }
                    else{
                        PlayerShipController.setTarget(clickPos);
                    }
                }
                return true;
            }
        });

        this.setFonts();

        renderView.resume();

        entityUpdater = new EntityUpdater(FPS);
        entityUpdater.setPirateShipSpawner(5000, 0.95, 2000, 600);
        entityUpdater.resume();

        DrawableFactory.init(renderView, entityUpdater);

        mapGrid = DrawableFactory.createMapGrid();
        city = DrawableFactory.createCity((ProgressBar)(findViewById(R.id.progressBarCityMorale)));
        addMoneyToCity(0);

        Button buttonCannon = (Button)findViewById(R.id.buttonCannon);
        buttonCannon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city.setBuildingCannon(true);
            }
        });

        Button buttonShip = (Button)findViewById(R.id.buttonShip);
        buttonShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city.buyNewShip();
            }
        });

        Button buttonSelectingShips = (Button)findViewById(R.id.buttonSelectShips);
        buttonSelectingShips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerShipController.setSelectingShips(!PlayerShipController.isSelectingShips());
            }
        });

        Button buttonEndGame = (Button)findViewById(R.id.buttonEndGame);
        buttonEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean musicPreference = preferences.getBoolean("notifications_music", false);
        if(musicPreference) {
            mainMusic.setLooping(true);
            mainMusic.start();
        }

        playingSounds = preferences.getBoolean("notifications_sounds", false);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        renderView.pause();
        entityUpdater.pause();
        mainMusic.stop();
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

    public static boolean isPlayingSounds(){
        return playingSounds;
    }

    public static void setAreaSize(Vector2D newAreaSize){
        areaSize = newAreaSize;
    }

    public static float getCityPosX(){return city.getPositionX();}

    public static float getCityOutsidePosX(){return city.getPositionXOutside();}

    public static void addMoneyToCity(int amount){
        game.addMoneyToCityPrivate(amount);
    }

    public static void endGame(){
        game.endGamePrivate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferences.getBoolean("example_switch", false))
            game.sendHighscore();
        renderView.pause();
        entityUpdater.pause();
    }

    private void sendHighscore(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!wifi.isConnected())
            return;
        final int highscore = city.getHighScore();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String nickname = preferences.getString("example_text", "");
        if(nickname.equals("")) {
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(HIGHSCORE_URL + "?nickname=" + nickname + "&score=" + highscore);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.getRequestMethod();
                    urlConnection.getInputStream();
                    urlConnection.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void endGamePrivate(){
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                layoutEndGame.setVisibility(View.VISIBLE);
                textViewScore.setText("Score: " + city.getHighScore());
            }
        });
    }

    private void addMoneyToCityPrivate(int amount){
        city.addMoney(amount);
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                TextView textView = (TextView)(findViewById(R.id.textViewMoney));
                textView.setText("Treasure: " + city.getMoney());
            }
        });
    }

    private void setFonts(){
        Typeface font = Typeface.createFromAsset(Game.getContext().getAssets(), "fonts/Kenney Pixel.ttf");

        TextView textView = (TextView)findViewById(R.id.textViewMoraleLabel);
        textView.setTypeface(font);

        textView = (TextView)findViewById(R.id.textViewMoney);
        textView.setTypeface(font);

        textView = (TextView)findViewById(R.id.textViewCityLost);
        textView.setTypeface(font);

        textView = (TextView)findViewById(R.id.textViewScore);
        textView.setTypeface(font);

        Button button = (Button)findViewById(R.id.buttonCannon);
        button.setTypeface(font);

        button = (Button)findViewById(R.id.buttonShip);
        button.setTypeface(font);

        button = (Button)findViewById(R.id.buttonSelectShips);
        button.setTypeface(font);

        button = (Button)findViewById(R.id.buttonEndGame);
        button.setTypeface(font);
    }
}
