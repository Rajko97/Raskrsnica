package com.raskrsnica.app;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    final static int FRAGMENT_SETTINGS = 1, FRAGMENT_DATABASE = 2;

    TextView tv1, tv2;
    ImageView img1, img2;
    LinearLayout btnSettings, btnDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSettings=(LinearLayout) findViewById(R.id.settingsbutton);
        btnDataBase=(LinearLayout) findViewById(R.id.databasebutton);
        tv1=(TextView)findViewById(R.id.settingstext);
        tv2=(TextView)findViewById(R.id.bazatext);
        img1=(ImageView)findViewById(R.id.imgSettings);
        img2=(ImageView)findViewById(R.id.imgBaza);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postaviFragment(FRAGMENT_SETTINGS);
            }
        });
        btnDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postaviFragment(FRAGMENT_DATABASE);
            }
        });
        postaviFragment(FRAGMENT_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE  && resultCode == RESULT_OK ) {
            postaviFragment(FRAGMENT_DATABASE);
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog2);
            final TextView tv1=dialog.findViewById(R.id.tv1);
            tv1.setText("Uputstvo!");
            final TextView tv2=dialog.findViewById(R.id.tv2);
            tv2.setText("Merenje je završeno. Izaberite podatke i kliknite na dugme za slanje na server.");
            dialog.setCancelable(false);
            Button btOk=dialog.findViewById(R.id.btOk);
            btOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }
    private void postaviFragment(int fragmentID) {
        android.support.v4.app.Fragment fragment = fragmentID == FRAGMENT_SETTINGS? new SettingsFragment(): new DataBaseFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        if (fragmentID == FRAGMENT_SETTINGS) {
            tv1.setTextColor(Color.rgb(217, 6, 71));
            tv2.setTextColor(Color.rgb(185, 185, 185));
            btnSettings.setBackgroundColor(Color.rgb(255, 255, 255));
            btnDataBase.setBackgroundResource(R.drawable.tab2);
            img1.setImageResource(R.drawable.ic_podesavanja_color);
            img2.setImageResource(R.drawable.ic_baza_grey);
        }
        else {
            tv1.setTextColor(Color.rgb(185, 185, 185));
            tv2.setTextColor(Color.rgb(217, 6, 71));
            btnDataBase.setBackgroundColor(Color.rgb(255, 255, 255));
            btnSettings.setBackgroundResource(R.drawable.tab1);
            img1.setImageResource(R.drawable.ic_podesavanja_grey);
            img2.setImageResource(R.drawable.ic_baza_color);
        }
    }

}