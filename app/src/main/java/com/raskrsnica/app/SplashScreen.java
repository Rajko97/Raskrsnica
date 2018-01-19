package com.raskrsnica.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pb=(ProgressBar)findViewById(R.id.progressBar);

        new Thread(new Runnable(){
            public void run(){
                doWork();
                startApp();
                finish();
            }
        }).start();
    }
    private void doWork(){
        for(int i=0;i<100;i+=10) {
            try{
                Thread.sleep(150);
                pb.setProgress(i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void startApp(){
        Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
        startActivity(intent);
    }
}
