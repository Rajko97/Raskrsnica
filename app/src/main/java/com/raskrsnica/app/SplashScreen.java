package com.raskrsnica.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.Scanner;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pb=(ProgressBar)findViewById(R.id.progressBar);

        new Thread(new Runnable(){
            public void run(){
                for(int i=0;i<100;i+=10) {
                    try{

                        Thread.sleep(150);
                        pb.setProgress(i);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                ucitajKorisnike();
                startApp();
                finish();
            }
        }).start();
    }
    private void ucitajKorisnike() {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.korisnici);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        try {
            JSONArray korisnici = new JSONArray(builder.toString());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Korisnici", korisnici.toString());
            editor.apply();
            ucitajZadatke(korisnici);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startApp();
    }

    private void ucitajZadatke(JSONArray korisnici) {

    }

    private void startApp(){
        Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
        startActivity(intent);
    }
}
