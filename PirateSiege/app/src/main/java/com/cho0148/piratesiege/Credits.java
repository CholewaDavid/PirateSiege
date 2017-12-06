package com.cho0148.piratesiege;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Credits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        setFonts();
    }

    private void setFonts(){
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/Kenney Pixel.ttf");

        TextView textView = (TextView)findViewById(R.id.textView9);
        textView.setTypeface(font);
        textView = (TextView)findViewById(R.id.textView10);
        textView.setTypeface(font);
        textView = (TextView)findViewById(R.id.textView11);
        textView.setTypeface(font);
        textView = (TextView)findViewById(R.id.textView12);
        textView.setTypeface(font);
        textView = (TextView)findViewById(R.id.textView13);
        textView.setTypeface(font);
        textView = (TextView)findViewById(R.id.textView14);
        textView.setTypeface(font);
        textView = (TextView)findViewById(R.id.textView15);
        textView.setTypeface(font);
        textView = (TextView)findViewById(R.id.textView16);
        textView.setTypeface(font);
    }
}
