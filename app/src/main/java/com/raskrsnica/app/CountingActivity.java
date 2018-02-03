package com.raskrsnica.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;


public class CountingActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, NumberPicker.OnValueChangeListener {
    private int[][] dugmiciVozila = {
        {R.id.btnVL1, R.id.btnVL2, R.id.btnVL3, R.id.btnVL4, R.id.btnVL5, R.id.btnVL6, R.id.btnVL7, R.id.btnVL8, R.id.btnVL9, R.id.btnVL10},
        {R.id.btnVP1, R.id.btnVP2, R.id.btnVP3, R.id.btnVP4, R.id.btnVP5, R.id.btnVP6, R.id.btnVP7, R.id.btnVP8, R.id.btnVP9, R.id.btnVP10},
        {R.id.btnVD1, R.id.btnVD2, R.id.btnVD3, R.id.btnVD4, R.id.btnVD5, R.id.btnVD6, R.id.btnVD7, R.id.btnVD8, R.id.btnVD9, R.id.btnVD10}
    };
    private int[][] textVozila = {
            {R.id.txtVL1, R.id.txtVL2, R.id.txtVL3, R.id.txtVL4, R.id.txtVL5, R.id.txtVL6, R.id.txtVL7, R.id.txtVL8, R.id.txtVL9, R.id.txtVL10},
            {R.id.txtVP1, R.id.txtVP2, R.id.txtVP3, R.id.txtVP4, R.id.txtVP5, R.id.txtVP6, R.id.txtVP7, R.id.txtVP8, R.id.txtVP9, R.id.txtVP10},
            {R.id.txtVD1, R.id.txtVD2, R.id.txtVD3, R.id.txtVD4, R.id.txtVD5, R.id.txtVD6, R.id.txtVD7, R.id.txtVD8, R.id.txtVD9, R.id.txtVD10}

    };
    private TextView[][] textViews = new TextView[3][10];
    private CountDownTimer countDownTimer;
    private int kvantum = 0;
    private int[][][] brojVozila = new int[4][3][10]; //4 kvantuma * 3 smera * 10 vozila

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        final TextView timer = (TextView) findViewById(R.id.kvantum);

        for (int[] ids: dugmiciVozila)
            for (int id : ids) {
                findViewById(id).setOnClickListener(this);
                findViewById(id).setOnLongClickListener(this);
            }

        for(int i = 0; i < textVozila.length; i++)
            for (int j = 0; j < textVozila[i].length; j++)
                textViews[i][j] = (TextView) findViewById(textVozila[i][j]);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String nazivRaskrsnice = b.getString("RASKRSNICA");
            String pozicija = b.getString("POZICIJA");
            String datum = b.getString("DATUM");
            String vreme = b.getString("VREME");
            String Levo = b.getString("SMER_LEVO");
            String Pravo = b.getString("SMER_PRAVO");
            String Desno = b.getString("SMER_DESNO");
            //TextView header = (TextView) findViewById(R.id.textHeader);
            //header.setText("[ "+datum+" : "+vreme+" ] RASKRSNICA: "+nazivRaskrsnice+", " + "smerovi:"+ (Levo.equals("0")? "":Levo+ "(Levo), ")+ (Pravo.equals("0")? "":Pravo+"(Pravo), ")+ (Desno.equals("0")? "":Desno+"(Desno)")+ "sa brojackog mesta: "+pozicija);


            // @drawable/circle
            if (Levo.equals("0")) {
                for (int i = 0; i < textVozila[2].length; i++) {
                    TextView ivVectorImage = (TextView) findViewById(textVozila[0][i]);
                    ivVectorImage.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
                if (Pravo.equals("0")) {
                    for (int i = 0; i < textVozila[2].length; i++) {
                        TextView ivVectorImage = (TextView) findViewById(textVozila[0][i]);
                        ivVectorImage.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
                if (Desno.equals("0")) {
                    for (int i = 0; i < textVozila[2].length; i++) {
                        TextView ivVectorImage = (TextView) findViewById(textVozila[2][i]);
                        ivVectorImage.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }


            //900000 default
            countDownTimer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long l) {
                    timer.setText("" + String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                }

                @Override
                public void onFinish() {
                    if (kvantum < 3) {
                        kvantum++;
                        for (int i = 0; i < textViews.length; i++)
                            for (int j = 0; j < textViews[0].length; j++)
                                textViews[i][j].setText("0");
                        start();
                    } else {
                        SacuvajPodatke();
                        Intent i = getIntent();
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            }.start();
        }

   private void SacuvajPodatke() {
        //todo da se podaci cuvaju u lokalnoj bazi
    }

    @Override
    public boolean onLongClick(View view) {
        for (int i = 0; i < dugmiciVozila.length; i++)
            for (int j = 0; j < dugmiciVozila[i].length; j++)
            if(view.getId() == dugmiciVozila[i][j])
            {
                showNumberPicker(i, j);
                return true;
            }
        return false;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < dugmiciVozila.length; i++)
            for (int j = 0; j < dugmiciVozila[i].length; j++)
                if(v.getId() == dugmiciVozila[i][j])
                {
                    textViews[i][j].setText(++brojVozila[kvantum][i][j] + "");
                    break;
                }
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
       // Log.i("value is",""+newVal);
    }
    public void showNumberPicker(final int voziloSmer, final int voziloID) {
        final Dialog d = new Dialog(CountingActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        final int minValue = -15;
        final int maxValue = 30;
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMinValue(0);
        np.setMaxValue(maxValue-minValue);
        np.setValue(0-minValue);
        np.setWrapSelectorWheel(false);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        String[] brojevi = new String[maxValue-minValue+1];
        for (int i = minValue; i  <= maxValue; i++)
            brojevi[i-minValue] = Integer.toString(i);
        np.setDisplayedValues(brojevi);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brojVozila[kvantum][voziloSmer][voziloID]+=np.getValue()+minValue;
                brojVozila[kvantum][voziloSmer][voziloID]=brojVozila[kvantum][voziloSmer][voziloID]>0?brojVozila[kvantum][voziloSmer][voziloID]:0;
                textViews[voziloSmer][voziloID].setText(brojVozila[kvantum][voziloSmer][voziloID]+"");
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onBackPressed() {
       new AlertDialog.Builder(this)
               .setTitle("Da li ste sigurni da želite da otkažete brojanje?")
               .setMessage("Ukoliko potvrdite, podaci koje ste merili neće biti sačuvani!")
               .setCancelable(false)
               .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       CountingActivity.this.finish();
                   }
               })
               .setNegativeButton("Ne", null)
               .show();
    }
}
/*Literatura:
    CountDownTimer
        https://developer.android.com/reference/android/os/CountDownTimer.html
        https://stackoverflow.com/questions/17620641/countdowntimer-in-minutes-and-seconds
    NumberPicker
        https://developer.android.com/reference/android/widget/NumberPicker.html
        https://stackoverflow.com/questions/17805040/how-to-create-a-number-picker-dialog
        https://stackoverflow.com/questions/14357520/android-numberpicker-negative-numbers
    INTENT
        -Prosledjivanje parametara: https://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter
    DUGMCI
        -Skraceni kod za puno dugmeta: https://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
        -OnLongClick event https://stackoverflow.com/questions/13382927/long-press-button-event-handler
    NIZ
        -obicna deklaracija -_-' https://stackoverflow.com/questions/1200621/how-do-i-declare-and-initialize-an-array-in-java
        -niz za textView https://stackoverflow.com/questions/31623126/how-to-put-textviews-in-an-array-and-findviewbyid-them
 */