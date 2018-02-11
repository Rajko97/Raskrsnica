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
import org.json.JSONObject;

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
                //todo Da se uskladi progress bar
                for(int i=0;i<100;i+=10) {
                    try{
                        Thread.sleep(150);
                        pb.setProgress(i);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //ucitajKorisnike();
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
        sacuvajKorisnike(builder.toString());
    }
    private void sacuvajKorisnike(String podaci) {
        try {
            SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
            JSONArray korisnici = new JSONArray(podaci);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Korisnici", korisnici.toString());
            editor.apply();
            ucitajZadatke(korisnici);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ucitajZadatke(JSONArray korisnici) {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.zadaci);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        sacuvajZadatke(korisnici, builder.toString());
    }

    private void sacuvajZadatke(JSONArray korisnici, String  s) {
        SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        try {
            JSONObject sviZadaci = new JSONObject(s);
            SharedPreferences.Editor editor = sharedPref.edit();

            for(int i = 0; i < korisnici.length(); i++) {
                JSONObject korisnik = new JSONObject(korisnici.get(i).toString());
                String ime = korisnik.getString("username");
                JSONArray zadaci = sviZadaci.getJSONArray("korisnik"+ime);
                editor.putString("Zadaci"+ime, zadaci.toString());
                editor.apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startApp(){
        Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
        startActivity(intent);
    }
}
