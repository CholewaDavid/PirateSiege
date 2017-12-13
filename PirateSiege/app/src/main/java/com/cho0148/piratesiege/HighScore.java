package com.cho0148.piratesiege;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HighScore extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!wifi.isConnected()) {
            Toast.makeText(getBaseContext(), "Not connected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if(!preferences.getBoolean("example_switch", false)) {
            Toast.makeText(getBaseContext(), "Highscore disabled", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        final StringBuilder highscoreText = new StringBuilder();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                List<HighscoreEntry> highscores = downloadData();
                Collections.sort(highscores, new Comparator<HighscoreEntry>(){
                    @Override
                    public int compare(HighscoreEntry entry1, HighscoreEntry entry2){
                        if(entry1.score < entry2.score)
                            return 1;
                        if(entry1.score > entry2.score)
                            return -1;
                        return 0;
                    }
                });
                String line;
                for(HighscoreEntry entry : highscores){
                    line = entry.nickname + ": " + entry.score + "\n";
                    highscoreText.append(line);
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView highscoreTextView = (TextView)findViewById(R.id.textViewHighscoreList);
        highscoreTextView.setText(highscoreText.toString());
        setFonts();
    }

    private List<HighscoreEntry> downloadData(){
        InputStream stream = null;
        List<HighscoreEntry> entries = null;

        try{
            stream = getInputStream();
            /*
            FileOutputStream fos = openFileOutput("highscore.txt", Context.MODE_PRIVATE);
            BufferedInputStream bufStream = new BufferedInputStream(stream);
            byte[] buffer = new byte[1024];
            int read;
            while((read = bufStream.read(buffer)) != -1){
                fos.write(buffer, 0, read);
            }

            fos.flush();
            fos.close();
            */
            entries = parseHighscoreData(stream);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return entries;
    }

    private InputStream getInputStream(){
        InputStream stream = null;
        try {
            URL url = new URL(Game.HIGHSCORE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            stream = conn.getInputStream();
        }catch(Exception e){
            e.printStackTrace();
        }
        return stream;
    }

    private List<HighscoreEntry> parseHighscoreData(InputStream stream) throws IOException, JSONException {
        /*
        File file = new File("highscore.txt");
        FileInputStream stream = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        stream.read();
        stream.close();
        */
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringDataBuilder = new StringBuilder();
        String line = "";
        while((line = br.readLine()) != null){
            stringDataBuilder.append(line);
        }
        String stringData = stringDataBuilder.toString();
        JSONArray jsonArray = new JSONArray(stringData);
        List<HighscoreEntry> output = new ArrayList<HighscoreEntry>();

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            HighscoreEntry entry = new HighscoreEntry();
            entry.nickname = jsonObject.getString("nickname");
            entry.score = jsonObject.getInt("score");
            output.add(entry);
        }

        return output;
    }

    private void setFonts(){
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/Kenney Pixel.ttf");

        TextView textView = (TextView)findViewById(R.id.textViewHighscoreList);
        textView.setTypeface(font);

        textView = (TextView)findViewById(R.id.textView2);
        textView.setTypeface(font);
    }
}
