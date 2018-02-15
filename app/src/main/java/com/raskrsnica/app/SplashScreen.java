package com.raskrsnica.app;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SplashScreen extends AppCompatActivity {
    private static String URL_ZA_KORISNIKE = "https://api.myjson.com/bins/g2lx9";
    private static String URL_ZA_ZADATKE = "https://api.myjson.com/bins/7mie5";

    private ProgressBar pb;
    RestClient restClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        restClient = RestClient.getInstance();
        pb = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            public void run() {
                //todo Da se uskladi progress bar
                for (int i = 0; i < 100; i += 10) {
                    try {
                        //Thread.sleep(150);
                        pb.setProgress(i);
                    } catch (Exception e) {
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
            //URL url = new URL("https://api.myjson.com/bins/g2lx9");
            String response = restClient.getRequest(URL_ZA_KORISNIKE);
            sacuvajKorisnike(response);



    ;/*
        final RequestQueue requestQueue = Volley.newRequestQueue(SplashScreen.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sacuvajKorisnike(response);
                        requestQueue.stop();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                requestQueue.stop();
            }
        });
        requestQueue.add(stringRequest);*/
       /* Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.korisnici);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        sacuvajKorisnike(builder.toString());*/
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

    private void ucitajZadatke(final JSONArray korisnici) {
        /*Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.zadaci);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        sacuvajZadatke(korisnici, builder.toString());*/
        /*
        String server_url = "https://api.myjson.com/bins/1afprx";
        final RequestQueue requestQueue = Volley.newRequestQueue(SplashScreen.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sacuvajZadatke(korisnici, response);
                        requestQueue.stop();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                requestQueue.stop();
            }
        });
        requestQueue.add(stringRequest);*/
        String response = restClient.getRequest(URL_ZA_ZADATKE);
        sacuvajZadatke(korisnici, response);
    }

    private void sacuvajZadatke(JSONArray korisnici, String s) {
        SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        try {
            JSONObject sviZadaci = new JSONObject(s);
            SharedPreferences.Editor editor = sharedPref.edit();

            for (int i = 0; i < korisnici.length(); i++) {
                JSONObject korisnik = new JSONObject(korisnici.get(i).toString());
                String ime = korisnik.getString("username");
                JSONArray zadaci = sviZadaci.getJSONArray("korisnik" + ime);
                editor.putString("Zadaci" + ime, zadaci.toString());
                editor.apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startApp() {
        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(intent);
    }
}