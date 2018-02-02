package com.raskrsnica.app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    final static int REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout bt1=(LinearLayout) findViewById(R.id.settingsbutton);
        final LinearLayout bt2=(LinearLayout) findViewById(R.id.databasebutton);
        final TextView tv1=(TextView)findViewById(R.id.settingstext);
        final TextView tv2=(TextView)findViewById(R.id.bazatext);
        final ImageView img1=(ImageView)findViewById(R.id.imgSettings);
        final ImageView img2=(ImageView)findViewById(R.id.imgBaza);


        android.support.v4.app.Fragment fragment = null;
        fragment = new SettingsFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.support.v4.app.Fragment fragment = new SettingsFragment();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                tv1.setTextColor(Color.rgb(217,6,71));
                tv2.setTextColor(Color.rgb(185,185,185));
                bt1.setBackgroundColor(Color.rgb(255,255,255));
                bt2.setBackgroundResource(R.drawable.tab2);
                img1.setImageResource(R.drawable.ic_podesavanja_color);
                img2.setImageResource(R.drawable.ic_baza_grey);

            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.Fragment fragment = new DataBaseFragment();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                tv1.setTextColor(Color.rgb(185,185,185));
                tv2.setTextColor(Color.rgb(217,6,71));
                bt2.setBackgroundColor(Color.rgb(255,255,255));
                bt1.setBackgroundResource(R.drawable.tab1);
                img1.setImageResource(R.drawable.ic_podesavanja_grey);
                img2.setImageResource(R.drawable.ic_baza_color);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE  && resultCode == RESULT_OK ) {
            android.support.v4.app.Fragment fragment = new DataBaseFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

}

/*
OnActivityResult
    http://android-er.blogspot.rs/2011/08/return-result-to-onactivityresult.html
    https://stackoverflow.com/questions/5302085/onactivityresult-never-called
  */