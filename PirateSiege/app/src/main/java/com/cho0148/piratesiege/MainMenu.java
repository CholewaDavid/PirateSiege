package com.cho0148.piratesiege;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {
    private Button btnStartGame;
    private Button btnShowHighscore;
    private Button btnShowCredits;
    private Button btnShowSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);

        btnStartGame = (Button)findViewById(R.id.buttonStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        btnShowHighscore = (Button)findViewById(R.id.buttonShowHighscore);
        btnShowHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHighscore();
            }
        });

        btnShowCredits = (Button)findViewById(R.id.buttonShowCredits);
        btnShowCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCredits();
            }
        });

        btnShowSettings = (Button)findViewById(R.id.buttonShowSettings);
        btnShowSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });

        setFonts();
    }

    private void startGame(){
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    private void showHighscore(){
        Intent intent = new Intent(this, HighScore.class);
        startActivity(intent);
    }

    private void showCredits(){
        Intent intent = new Intent(this, Credits.class);
        startActivity(intent);
    }

    private void showSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void setFonts(){
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/Kenney Pixel.ttf");

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setTypeface(font);

        btnStartGame.setTypeface(font);
        btnShowHighscore.setTypeface(font);
        btnShowCredits.setTypeface(font);
    }
}
