package com.raskrsnica.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;


public class CountingActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    public boolean b=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);
        final TextView timer=(TextView)findViewById(R.id.kvantum);
        Button bt=(Button)findViewById(R.id.stop);
        Button bt_nazad=(Button)findViewById(R.id.nazad);
        Button bt_baza=(Button)findViewById(R.id.baza);


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
                    Intent intent=new Intent(CountingActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
        }
}

/*Literatura:
    CountDownTimer
        https://developer.android.com/reference/android/os/CountDownTimer.html
        https://stackoverflow.com/questions/17620641/countdowntimer-in-minutes-and-seconds
    INTENT
    -Prosledjivanje parametara: https://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter
 */