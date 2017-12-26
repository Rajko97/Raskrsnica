package com.raskrsnica.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;
public class CountingActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] dugmiciVozila = {R.id.vozilo1, R.id.vozilo2, R.id.vozilo3, R.id.vozilo4, R.id.vozilo5, R.id.vozilo6, R.id.vozilo7, R.id.vozilo8, R.id.vozilo9, R.id.vozilo10};
    private int[] textVozila = {R.id.brojVozila1, R.id.brojVozila2, R.id.brojVozila3, R.id.brojVozila4, R.id.brojVozila5, R.id.brojVozila6, R.id.brojVozila7, R.id.brojVozila8, R.id.brojVozila9, R.id.brojVozila10};
    private int[] brojVozilaLevo = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] brojVozilaPravo = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] brojVozilaDesno = new int[10]; //Drugi nacin, isto je
    TextView[] textViews = new TextView[10];


    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);
        final TextView timer = (TextView) findViewById(R.id.kvantum);
        Button bt = (Button) findViewById(R.id.stop);
        Button bt_nazad = (Button) findViewById(R.id.nazad);
        Button bt_baza = (Button) findViewById(R.id.baza);


        for (int id : dugmiciVozila) {
            findViewById(id).setOnClickListener(this);
        for (int i2 = 0; i2 < 10; i2++) {
            textViews[i2] = (TextView) findViewById(textVozila[i2]);
        }
            countDownTimer= new CountDownTimer(900000, 1000) {
                int i=0;
                @Override
                public void onTick(long l) {
                    timer.setText("" + String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                }

                @Override
                public void onFinish() {

                    if(i<3)
                    {
                        start();
                        i++;
                    }
                }
            }.start();

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countDownTimer.cancel(); //Dugme prekida kvnatum skroz
                }
            });

            bt_nazad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CountingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public void onClick(View v) {

        int indeks = -1; //trazimo indeks dugmeta koje smo pritisnuli
        for (indeks = 0; indeks < 10; indeks++)
            if(v.getId() == dugmiciVozila[indeks])
                break;
        if (indeks == -1) // ako nije u nizu dugmica vozila
            return;        // zanemarujemo ga
        //TextView tekst =  v.findViewById(textVozila[indeks]);
        //switch(IzabraniSmer)
            //case Levo:
                // break;
            //case Pravo:
                // break;
            //case Desno:
                brojVozilaDesno[indeks]++;
                textViews[indeks].setText(brojVozilaDesno[indeks]+"");


            // break;

    }
}

/*Literatura:
    CountDownTimer
        https://developer.android.com/reference/android/os/CountDownTimer.html
        https://stackoverflow.com/questions/17620641/countdowntimer-in-minutes-and-seconds
    INTENT
    -Prosledjivanje parametara: https://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter
    DUGMCI
        -Skraceni kod za puno dugmeta: https://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
    NIZ
        -obicna deklaracija -_-' https://stackoverflow.com/questions/1200621/how-do-i-declare-and-initialize-an-array-in-java
        -niz za textView https://stackoverflow.com/questions/31623126/how-to-put-textviews-in-an-array-and-findviewbyid-them
 */