package com.raskrsnica.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private int kvantum = 0;
    private int trajanje = 1;
    private int pozicija = 0;
    private int assigment_id = 0;
    private int[][][] brojVozila;
    private boolean[] ukljucenSmer = {false, false, false};

    String smerID[] = new String[3];
    String nazivRaskrsnice, datum, vreme;
    CountDownTimer countDownTimer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            nazivRaskrsnice = b.getString("RASKRSNICA");
            pozicija = Integer.valueOf(b.getString("POZICIJA"));
            trajanje = Integer.valueOf(b.getString("TRAJANJE"));
            datum = b.getString("DATUM");
            vreme = b.getString("VREME");
            smerID[0] = b.getString("SMER_LEVO");
            smerID[1] = b.getString("SMER_PRAVO");
            smerID[2] = b.getString("SMER_DESNO");
            assigment_id = Integer.valueOf(b.getString("ZADATAK_ID"));
            //TextView header = (TextView) findViewById(R.id.textHeader);
            //header.setText("[ "+datum+" : "+vreme+" ] RASKRSNICA: "+nazivRaskrsnice+", " + "smerovi:"+ (Levo.equals("0")? "":Levo+ "(Levo), ")+ (Pravo.equals("0")? "":Pravo+"(Pravo), ")+ (Desno.equals("0")? "":Desno+"(Desno)")+ "sa brojackog mesta: "+pozicija);

            brojVozila = new int[4 * trajanje][3][10]; //4 kvantuma * 3 smera * 10 vozila
            if (!smerID[0].equals("0")) ukljucenSmer[0] = true;
            if (!smerID[1].equals("0")) ukljucenSmer[1] = true;
            if (!smerID[2].equals("0")) ukljucenSmer[2] = true;
        }
        podesiIzgled();
        final Dialog d = new Dialog(CountingActivity.this);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCancelable(false);
        d.setContentView(R.layout.timer);
        final TextView tv1=d.findViewById(R.id.tv1);
        d.setCancelable(false);
        ImageButton img=d.findViewById(R.id.imgb);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer2.cancel();
                finish();
            }
        });
        d.show();

        countDownTimer2 = new CountDownTimer(getRemainingTimeinMS(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    long mills = getRemainingTimeinMS();
                    //int dani = (int) TimeUnit.MILLISECONDS.toDays(mills);
                    int sati = (int) TimeUnit.MILLISECONDS.toHours(mills);
                    int minuti = (int) TimeUnit.MILLISECONDS.toMinutes(mills) % 60;
                    int sekunde = (int) TimeUnit.MILLISECONDS.toSeconds(mills) % 60;
                    DecimalFormat f=new DecimalFormat("00");
                    String diff = f.format(sati)+" : "+f.format(minuti)+" : "+f.format(sekunde);
                    tv1.setText(diff);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                d.dismiss();
                pocniBrojanje();
            }
        }.start();
    }
    private long getRemainingTimeinMS() {
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy HH:mm");
        //sdf.setTimeZone(TimeZone.getTimeZone("UCT"));
        Date now = Calendar.getInstance().getTime();
        Date zadatak = null;
        try {
            zadatak = sdf.parse(datum+" "+vreme);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return zadatak.getTime()-now.getTime();
    }
    protected void podesiIzgled() {
        for (int[] ids: dugmiciVozila)
            for (int id : ids) {
                findViewById(id).setOnClickListener(this);
                findViewById(id).setOnLongClickListener(this);
            }

        for (int i = 0; i < textVozila.length; i++)
            if (ukljucenSmer[i])
                for (int j = 0; j < textVozila[i].length; j++) {
                    textViews[i][j] = (TextView) findViewById(textVozila[i][j]);
                }

        int[] ikoniceVozila = {
                R.drawable.ic_bicikl, R.drawable.ic_motor, R.drawable.ic_auto, R.drawable.ic_autobus,
                R.drawable.ic_laka_teretna, R.drawable.ic_laka_teretna, R.drawable.ic_srednja_teretna, R.drawable.ic_teska_teretna,
                R.drawable.ic_autovoz, R.drawable.ic_traktor, R.drawable.ic_zaprega
        };
        for (int i = 0; i < 3; i++) {
            if (ukljucenSmer[i])
                continue;
            for (int j = 0; j < textVozila[i].length; j++) {
                TextView text = (TextView) findViewById(textVozila[i][j]);
                Button dugme = (Button) findViewById(dugmiciVozila[i][j]);
                ImageView img = null;
                switch (i) {
                    case 0:
                        img = (ImageView) findViewById(R.id.imgStrelicaLevo);
                        break;
                    case 1:
                        img = (ImageView) findViewById(R.id.imgStrelicaPravo);
                        break;
                    case 2:
                        img = (ImageView) findViewById(R.id.imgStrelicaDesno);
                        break;
                }

                Drawable mDrawable = getResources().getDrawable(ikoniceVozila[j]);
                mDrawable.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
                mDrawable = DrawableCompat.wrap(mDrawable);
                int h = mDrawable.getIntrinsicHeight();
                int w = mDrawable.getIntrinsicWidth();
                mDrawable.setBounds(0, 0, w, h);
                dugme.setCompoundDrawablesRelative(mDrawable, null, null, null);

                dugme.setBackgroundResource(R.drawable.circle_grey);
                text.setBackgroundResource(R.drawable.circle_grey);

                text.setTextColor(getResources().getColor(R.color.colorDisabledGrey));
                dugme.setTextColor(getResources().getColor(R.color.colorDisabledGrey));
                img.setColorFilter(getResources().getColor(R.color.colorDisabledGrey));
            }
        }
    }
    protected void pocniBrojanje() {
        final TextView timer = (TextView) findViewById(R.id.kvantum);

        CountDownTimer countDownTimer = new CountDownTimer(900000, 1000) { //900000 default
            @Override
            public void onTick(long l) {
                timer.setText("" + String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes(l), TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
            }
            @Override
            public void onFinish() {
                if (kvantum < (trajanje * 4 - 1)) {
                    kvantum++;
                    if (kvantum % 4 == 0) {
                        SacuvajPodatke();
                    }
                    for (int i = 0; i < textViews.length; i++)
                        if (ukljucenSmer[i])
                            for (int j = 0; j < textViews[0].length; j++)
                                textViews[i][j].setText("0");
                    start();
                    timer.setText("15 : 00");
                } else {
                    kvantum++;
                    for (int i = 0; i < 3; i++)
                        ukljucenSmer[i]=false;

                    SacuvajPodatke();
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
        }.start();
    }
    private void SacuvajPodatke() {
       SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
       try {
           String korisnik = sharedPref.getString("UlogovanKorisnik", "");
           String jsonData = sharedPref.getString("Merenja"+korisnik, "0");
           JSONArray merenja;
           if (!jsonData.equals("0"))
               merenja = new JSONArray(jsonData);
           else
               merenja = new JSONArray();

           JSONArray noviJson = new JSONArray();
           JSONObject merenje = new JSONObject();


           try {//json merenje unutar njega json podaci sadrzi atribut id i objekte q1 q2 q3 q4
               //svaki objekat q sadrzi atribut direction i objekat count
               merenje.put("Naziv", nazivRaskrsnice);
               merenje.put("Datum", datum);
               merenje.put("Vreme", vreme);
               merenje.put("Otpremljeno", "false");

               JSONObject podaci = new JSONObject();
               podaci.put("assignment_id", ""+assigment_id);

               for (int iKvantum = 1; iKvantum < 5; iKvantum++)
               {
                   JSONArray jsonKvantum = new JSONArray();
                   JSONObject jsonSmer = new JSONObject();
                   for (int iSmer = 0; iSmer < 3; iSmer++) {
                       if (!ukljucenSmer[iSmer])
                           continue;

                       JSONArray jsonVrednosti = new JSONArray();
                       for (int iVozilo = 0; iVozilo < 10; iVozilo++) {
                           jsonVrednosti.put(brojVozila[4*(kvantum / 4 - 1)+(iKvantum-1)][iSmer][iVozilo]);
                       }
                       jsonSmer.put("direction",""+((pozicija+iSmer)%4+1));
                       jsonSmer.put("count", jsonVrednosti);
                       jsonKvantum.put(jsonSmer);
                   }
                   podaci.put("q"+iKvantum, jsonKvantum);
               }
               merenje.put("Podaci", podaci);

               SimpleDateFormat format = new SimpleDateFormat("HH:mm");
               Date pocetnoVreme = format.parse(vreme);
               //todo getTime vraca 1 sat manje??
               long trenutnoVreme = (pocetnoVreme.getTime()+2*1000*60*60)/1000;
               int hours = (int) (trenutnoVreme / 3600)%24;
               vreme = hours+":00";
               if(vreme.equals("0:00"))
               {
                   format = new SimpleDateFormat("d.M.yyyy");
                   Calendar cal = Calendar.getInstance();
                   cal.setTime(format.parse(datum));
                   cal.add(Calendar.DATE, 1);
                   datum = format.format(cal.getTime());
               }
           } catch (JSONException e) {
               e.printStackTrace();
           } catch (ParseException e) {
               e.printStackTrace();
           }
           noviJson.put(merenje);

           for (int i = 0; i < merenja.length(); i++)
               noviJson.put(merenja.get(i));

           SharedPreferences.Editor editor = sharedPref.edit();
           editor.putString("Merenja"+korisnik, noviJson.toString());
           editor.apply();

       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

    @Override
    public boolean onLongClick(View view) {
        for (int i = 0; i < dugmiciVozila.length; i++)
            if(ukljucenSmer[i])
                for (int j = 0; j < dugmiciVozila[i].length; j++)
                    if(view.getId() == dugmiciVozila[i][j])
                    {
                        prikaziTastaturu(i, j);
                        return true;
                    }
        return false;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < dugmiciVozila.length; i++)
            if(ukljucenSmer[i])
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
    public void prikaziTastaturu(final int voziloSmer, final int voziloID) {
        final Dialog d = new Dialog(CountingActivity.this);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setContentView(R.layout.keyboard);
        final TextView ekran = (TextView) d.findViewById(R.id.tvEkran);
        int buttons[] = {R.id.btnAdd0, R.id.btnAdd1, R.id.btnAdd2, R.id.btnAdd3, R.id.btnAdd4,
                R.id.btnAdd5, R.id.btnAdd6, R.id.btnAdd7, R.id.btnAdd8, R.id.btnAdd9, R.id.btnConfirm};
        for (int i = 0; i < buttons.length; i++) {
            Button button = (Button) d.findViewById(buttons[i]);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 10) {
                        if(!ekran.getText().toString().equals(""))
                            brojVozila[kvantum][voziloSmer][voziloID]+= Integer.parseInt(ekran.getText().toString());
                        textViews[voziloSmer][voziloID].setText(brojVozila[kvantum][voziloSmer][voziloID]+"");
                        d.dismiss();
                    }
                    else {
                        if (ekran.getText().toString().equals("0"))
                            ekran.setText(""+finalI);
                        else
                            ekran.append("" + finalI);
                    }
                }
            });
        }
        ImageButton button = (ImageButton) d.findViewById(R.id.btnDelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String str = ekran.getText().toString();
                    if(str.length() > 0)
                        ekran.setText(str.substring(0, str.length()-1));
            }
        });
        d.show();
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
        final Dialog dialog = new Dialog(CountingActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog1);
        final TextView tv1=dialog.findViewById(R.id.tv1);
        tv1.setText("Da li ste sigurni da želite da otkažete brojanje?");
        final TextView tv2=dialog.findViewById(R.id.tv2);
        tv2.setText("Ukoliko potvrdite, podaci koje ste merili neće biti sačuvani!");
        dialog.setCancelable(false);
        Button btDa=dialog.findViewById(R.id.btDa);
        btDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountingActivity.this.finish();
            }
        });
        Button btNe=dialog.findViewById(R.id.btNe);
        btNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}