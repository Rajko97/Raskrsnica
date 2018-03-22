package com.raskrsnica.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashScreen extends AppCompatActivity {
    //private static String URL_ZA_KORISNIKE = "https://api.myjson.com/bins/g2lx9";
    private static String URL_ZA_KORISNIKE = "http://192.168.0.106:8000/api/allUsers";
    private static String URL_ZA_ZADATKE = "https://api.myjson.com/bins/16f685";
    private ImageView logo,logo2;
    private ProgressBar pb;
    private TextView tv;
    RestClient restClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        logo=(ImageView)findViewById(R.id.imageView);
        logo2=(ImageView)findViewById(R.id.imgAppsLogo);
        tv=(TextView)findViewById(R.id.tvLoading);
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.fade_in);
        logo.startAnimation(animation);
        tv.startAnimation(animation);
        logo2.startAnimation(animation);

        restClient = RestClient.getInstance();
        pb = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            public void run() {
                ucitajKorisnike();
                pb.setProgress(100);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startApp();
                finish();
            }
        }).start();
    }

    private void ucitajKorisnike() {
            String response = restClient.getRequest(URL_ZA_KORISNIKE);
            pb.setSecondaryProgress(20);
            sacuvajKorisnike(response);
    }

    private void sacuvajKorisnike(String podaci) {
        try {
            SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
            JSONArray korisnici = new JSONArray(podaci);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Korisnici", korisnici.toString());
            editor.apply();
            pb.setProgress(20);
            ucitajZadatke(korisnici);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ucitajZadatke(JSONArray korisnici) {
        String response = restClient.getRequest(URL_ZA_ZADATKE);
        pb.setSecondaryProgress(40);
        sacuvajZadatke(korisnici, response);
    }

    private void ucitajSlike(JSONArray zadaci) {
        try {
            for (int i = 0; i < zadaci.length(); i++) {
                JSONObject zadatak = new JSONObject(zadaci.get(i).toString());
                String url_slike = zadatak.getString("Slika");

                String imagename = null;
                for (int j = url_slike.length()-1; j >= 0; j--) {
                    if(url_slike.charAt(j) == '/') {
                        imagename = url_slike.substring(j+1, url_slike.length()-4);
                        break;
                    }
                }
                downloadFile(url_slike, imagename);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                pb.incrementProgressBy((int) (80.0/korisnici.length()));
                pb.incrementSecondaryProgressBy((int) (60.0/korisnici.length()));
                ucitajSlike(zadaci);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startApp() {
        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(intent);
    }
    public void downloadFile(String uRl, String filename) {
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(uRl, filename);
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                //
                String pathOnSd = Environment.getExternalStorageDirectory()+"/.Raskrsnica/";
                File new_folder = new File(pathOnSd);
                if(!new_folder.exists()) {
                    new_folder.mkdir();
                }


                File input_file = new File(new_folder, params[1]+".jpg");
                if (!input_file.exists()) {
                    InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                    byte[] data = new byte[1024];
                    int total = 0;
                    int count = 0;
                    OutputStream outputStream = new FileOutputStream(input_file);
                    while ((count = inputStream.read(data)) != -1) {
                        total += count;
                        outputStream.write(data, 0, count);
                    /*int progress = (int) total*100/file_length;
                    */
                    }
                    inputStream.close();
                    outputStream.close();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download success";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //progressDialog.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            //proressDialog.hide();
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            /*String path = "sdcard/Raskrsnica/downloaded_image.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(path));*/
        }
    }
}