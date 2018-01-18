package com.raskrsnica.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
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

import java.util.concurrent.TimeUnit;
public class CountingActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, NumberPicker.OnValueChangeListener {

    private int[] dugmiciVozila = {R.id.vozilo1, R.id.vozilo2, R.id.vozilo3, R.id.vozilo4, R.id.vozilo5, R.id.vozilo6, R.id.vozilo7, R.id.vozilo8, R.id.vozilo9, R.id.vozilo10};
    private int[] textVozila = {R.id.brojVozila1, R.id.brojVozila2, R.id.brojVozila3, R.id.brojVozila4, R.id.brojVozila5, R.id.brojVozila6, R.id.brojVozila7, R.id.brojVozila8, R.id.brojVozila9, R.id.brojVozila10};
    private int[] brojVozilaLevo = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] brojVozilaPravo = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] brojVozilaDesno = new int[10]; //Drugi nacin, isto je
    private int izabraniSmer = -1;
    TextView[] textViews = new TextView[10];

    CountDownTimer countDownTimer;
 //Todo kada korisnik zadrzi dugme i ono kad moze da doda 5 odjednom naprimer i kad moze da oduzima, mora da se stavi da ne ide u minus(ispod nule), nego samo do nule
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        final TextView timer = (TextView) findViewById(R.id.kvantum);
        Button bt_stop = (Button) findViewById(R.id.stop);
        Button bt_nazad = (Button) findViewById(R.id.nazad);
        Button bt_baza = (Button) findViewById(R.id.baza);

        final ToggleButton tb[] = new ToggleButton[3];
        tb[0] = (ToggleButton) findViewById(R.id.toggleButtonLevo);
        tb[1] = (ToggleButton) findViewById(R.id.toggleButtonPravo);
        tb[2] = (ToggleButton) findViewById(R.id.toggleButtonDesno);

        for (int id : dugmiciVozila) {
            findViewById(id).setOnClickListener(this);
            findViewById(id).setOnLongClickListener(this);
        }
        for (int i2 = 0; i2 < 10; i2++) {
            textViews[i2] = (TextView) findViewById(textVozila[i2]);
        }

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String nazivRaskrsnice = b.getString("RASKRSNICA");
            String pozicija = b.getString("POZICIJA");
            String datum = b.getString("DATUM");
            String vreme = b.getString("VREME");
            String Levo = b.getString("SMER_LEVO");
            String Pravo = b.getString("SMER_PRAVO");
            String Desno = b.getString("SMER_DESNO");
            TextView header = (TextView) findViewById(R.id.textHeader);
            header.setText("[ "+datum+" : "+vreme+" ] RASKRSNICA: "+nazivRaskrsnice+", " + "smerovi:"+ (Levo.equals("0")? "":Levo+ "(Levo), ")+ (Pravo.equals("0")? "":Pravo+"(Pravo), ")+ (Desno.equals("0")? "":Desno+"(Desno)")+ "sa brojackog mesta: "+pozicija);
            LinearLayout lin1 = (LinearLayout) findViewById(R.id.ukljuciLevo);
            LinearLayout lin2 = (LinearLayout) findViewById(R.id.ukljuciPravo);
            LinearLayout lin3 = (LinearLayout) findViewById(R.id.ukljuciDesno);
            TextView text1 = (TextView) findViewById(R.id.textLevo);
            TextView text2 = (TextView) findViewById(R.id.textPravo);
            TextView text3 = (TextView) findViewById(R.id.textDesno);

            if(Desno.equals("0"))
                lin3.setVisibility(View.GONE);
            else {
                izabraniSmer = 2;
                text3.setText(Desno);
            }
            if (Levo.equals("0"))
                lin1.setVisibility(View.GONE);
            else {
                izabraniSmer = 0;
                text1.setText(Levo);
            }
            if(Pravo.equals("0"))
                lin2.setVisibility(View.GONE);
            else {
                izabraniSmer = 1;
                text2.setText(Pravo);
            }
            tb[izabraniSmer].setChecked(true);
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

        bt_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countDownTimer.cancel(); //Dugme prekida kvnatum skroz
                    AlertDialog alertDialog = new AlertDialog.Builder(CountingActivity.this).create();
                    alertDialog.setTitle("Obavestenje");
                    alertDialog.setMessage("Zaustavili ste brojanje.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
        });
        bt_nazad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(CountingActivity.this, MainActivity.class);
                    //startActivity(intent);
                    finish();
                }
            });
        bt_baza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i= getIntent();
                setResult(RESULT_OK, i);
                finish();
            }
        });
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            tb[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b == true) {
                        tb[(finalI +1)%3].setChecked(false);
                        tb[(finalI +2)%3].setChecked(false);
                        izabraniSmer = finalI;
                        PostaviTabelu();
                    }
                    else {
                        if (!tb[(finalI +1)%3].isChecked() && !tb[(finalI +2)%3].isChecked())
                            tb[finalI].setChecked(true);
                    }
                }
            });
        }
    }

    private void PostaviTabelu() {
        switch (izabraniSmer) {
            case 0:
                for (int i = 0; i <10; i++)
                    textViews[i].setText(brojVozilaLevo[i] + "");
                break;
            case 1:
                for (int i = 0; i <10; i++)
                    textViews[i].setText(brojVozilaPravo[i] + "");
                break;
            case 2:
                for (int i = 0; i <10; i++)
                    textViews[i].setText(brojVozilaDesno[i] + "");
                break;
        }
    }


    @Override
    public boolean onLongClick(View view) {
        for (int i = 0; i < 10; i++)
            if(view.getId() == dugmiciVozila[i])
            {
                showNumberPicker(i);
                return true;
            }
        return false;
    }

    @Override
    public void onClick(View v) {
        int indeks = -1; //trazimo indeks dugmeta koje smo pritisnuli
        for (int i = 0; i < 10; i++)
            if(v.getId() == dugmiciVozila[i])
            {
                indeks = i;
                break;
            }
        if (indeks == -1) // ako nije u nizu dugmica vozila
            return;        // zanemarujemo ga

        switch(izabraniSmer) {
            case 0:
                brojVozilaLevo[indeks]++;
                textViews[indeks].setText(brojVozilaLevo[indeks] + "");
                break;
            case 1:
                brojVozilaPravo[indeks]++;
                textViews[indeks].setText(brojVozilaPravo[indeks] + "");
                break;
            case 2:
                brojVozilaDesno[indeks]++;
                textViews[indeks].setText(brojVozilaDesno[indeks] + "");
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
    }
    public void showNumberPicker(final int voziloID) {
        final Dialog d = new Dialog(CountingActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
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
                brojVozilaDesno[voziloID]+=np.getValue()+minValue;
                textViews[voziloID].setText(brojVozilaDesno[voziloID]+"");
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
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