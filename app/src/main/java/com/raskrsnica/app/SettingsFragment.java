package com.raskrsnica.app;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment {

    final static int REQ_CODE = 1, NAZIV = 0, BR_MESTO = 1, DATUM = 2, POCETAK = 3, TRAJANJE = 4, SMER_LEVO = 5, SMER_PRAVO = 6, SMER_DESNO = 7;
    
    public SettingsFragment() {
        // Required empty public constructor
    }

    Spinner spinner;
    CountDownTimer countDownTimer;
    String strLevo, strPravo, strDesno;
    String[][] podaciRaskrsnice;
    Boolean OtvorenSpiner = false;
    Boolean greska = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        String korisnik = sharedPref.getString("UlogovanKorisnik", "");
        try {
            JSONArray zadaci = new JSONArray(sharedPref.getString("Zadaci"+korisnik, ""));
            podaciRaskrsnice = new String[9][zadaci.length()];
            for (int i = 0; i < zadaci.length(); i++) {
                JSONObject zadatak = new JSONObject(zadaci.get(i).toString());
                podaciRaskrsnice[NAZIV][i] = zadatak.getString("Raskrsnica");
                podaciRaskrsnice[BR_MESTO][i] = zadatak.getString("BrMesto");
                podaciRaskrsnice[DATUM][i] = zadatak.getString("Datum");
                podaciRaskrsnice[POCETAK][i] = zadatak.getString("Vreme");
                podaciRaskrsnice[TRAJANJE][i] = zadatak.getString("Trajanje");
                podaciRaskrsnice[SMER_LEVO][i] = zadatak.getString("SmerLevo");
                podaciRaskrsnice[SMER_PRAVO][i] = zadatak.getString("SmerPravo");
                podaciRaskrsnice[SMER_DESNO][i] = zadatak.getString("SmerDesno");
            }
        } catch (JSONException e) {
            greska = true;
            podaciRaskrsnice = new String[1][1];
            podaciRaskrsnice[0][0] = "Nemate nijedan zadatak";
            e.printStackTrace();
        }

        spinner=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(rootView.getContext(), R.layout.view_spinner_item, podaciRaskrsnice[0]);
        adapter.setDropDownViewResource(R.layout.dropdownlist_style);
        spinner.setAdapter(adapter);
        if(!greska)
            postaviDefaultVrednost(spinner);

        final TextView tvNaziv, tvBrMesto, tvSmerovi, tvDatum, tvPocetak, tvTrajanje;
        tvNaziv = (TextView) rootView.findViewById(R.id.tvNaziv);
        tvBrMesto = (TextView) rootView.findViewById(R.id.tvBrMesto);
        tvSmerovi = (TextView) rootView.findViewById(R.id.tvSmerovi);
        tvDatum = (TextView) rootView.findViewById(R.id.tvDatum);
        tvPocetak = (TextView) rootView.findViewById(R.id.tvPocetak);
        tvTrajanje = (TextView) rootView.findViewById(R.id.tvTrajanje);

        final Button button=(Button)rootView.findViewById(R.id.btNext);

        if(!greska) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tvNaziv.setText(podaciRaskrsnice[NAZIV][i]);
                    tvBrMesto.setText(podaciRaskrsnice[BR_MESTO][i]);
                    tvDatum.setText(podaciRaskrsnice[DATUM][i]);
                    tvPocetak.setText(podaciRaskrsnice[POCETAK][i]);
                    tvTrajanje.setText(podaciRaskrsnice[TRAJANJE][i] + "h");
                    PostaviIndekseSmerova(Integer.parseInt(podaciRaskrsnice[BR_MESTO][i]));
                    tvSmerovi.setText((podaciRaskrsnice[SMER_LEVO][i].equals("1") ? "Levo(" + strLevo + ") " : "")
                            + (podaciRaskrsnice[SMER_PRAVO][i].equals("1") ? "Pravo(" + strPravo + ") " : "")
                            + (podaciRaskrsnice[SMER_DESNO][i].equals("1") ? "Desno(" + strDesno + ") " : ""));

                    if (OtvorenSpiner)
                        spinner.setBackgroundResource(R.drawable.spinner_background);
                    OtvorenSpiner = false;

                    SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy HH:mm");
                    try {
                        Date zadatak = sdf.parse(podaciRaskrsnice[DATUM][i]+" "+podaciRaskrsnice[POCETAK][i]);
                        Date now = Calendar.getInstance().getTime();
                        if(zadatak.after(now))
                            ukljuciDugme(button);
                        else
                            iskljuciDugme(button);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                private void PostaviIndekseSmerova(int pozicija) {
                    strLevo = pozicija + "." + (pozicija % 4 + 1);
                    strPravo = pozicija + "." + ((pozicija + 1) % 4 + 1);
                    strDesno = pozicija + "." + ((pozicija + 2) % 4 + 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    if (OtvorenSpiner)
                        spinner.setBackgroundResource(R.drawable.spinner_background);
                    OtvorenSpiner = false;
                }
            });

            spinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (OtvorenSpiner) {
                            spinner.setBackgroundResource(R.drawable.spinner_background);
                            OtvorenSpiner = false;
                        } else {
                            spinner.setBackgroundResource(R.drawable.spinner_background2);
                            OtvorenSpiner = true;
                        }
                    }
                    return false;
                }
            });
        }
        else
            spinner.setEnabled(false);
        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                spinner.setDropDownVerticalOffset(spinner.getDropDownVerticalOffset() + spinner.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    spinner.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    spinner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                @Override
                public void onClick(View view) {
                    final int i = spinner.getSelectedItemPosition();
                    if (istekloVreme(podaciRaskrsnice[DATUM][i]+" "+podaciRaskrsnice[POCETAK][i])) {
                        new AlertDialog.Builder(getActivity())
                            .setTitle("Greska!")
                            .setMessage("Zakasnili ste za ovo merenje!")
                            .setPositiveButton("OK", null)
                            .show();
                        iskljuciDugme(button);
                    }
                    else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("Vreme do brojanja");
                        alertDialog.setMessage("00:00:00");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Otkazi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                countDownTimer.cancel();
                                dialogInterface.dismiss();
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Otkazali ste brojanje", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                         });
                        alertDialog.show();
                        final long mills = getRemainingTimeinMS(i);
                        countDownTimer = new CountDownTimer(mills, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                try {
                                    long mills = getRemainingTimeinMS(i);
                                    int dani = (int) TimeUnit.MILLISECONDS.toDays(mills);
                                    int sati = (int) TimeUnit.MILLISECONDS.toHours(mills) % 24;
                                    int minuti = (int) TimeUnit.MILLISECONDS.toMinutes(mills) % 60;
                                    int sekunde = (int) TimeUnit.MILLISECONDS.toSeconds(mills) % 60;
                                    String diff = (dani>0?"Za "+dani+" dana i ":"")+sati+":"+minuti+":"+sekunde;
                                    alertDialog.setMessage(diff);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFinish() {
                                alertDialog.dismiss();
                                PokreniBrojanje();
                            }
                        }.start();

                    }
                }

                private long getRemainingTimeinMS(int i) {
                    SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy HH:mm");
                    //sdf.setTimeZone(TimeZone.getTimeZone("UCT"));
                    Date now = Calendar.getInstance().getTime();
                    Date zadatak = null;
                    try {
                        zadatak = sdf.parse(podaciRaskrsnice[DATUM][i]+" "+podaciRaskrsnice[POCETAK][i]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return zadatak.getTime()-now.getTime();
                }

                private void PokreniBrojanje() {
                    Intent intent = new Intent(getActivity(), CountingActivity.class);
                    Bundle b = new Bundle();
                    int i = spinner.getSelectedItemPosition();
                    b.putString("RASKRSNICA", podaciRaskrsnice[NAZIV][i]);
                    b.putString("POZICIJA", podaciRaskrsnice[BR_MESTO][i]);
                    b.putString("DATUM", podaciRaskrsnice[DATUM][i]);
                    b.putString("VREME", podaciRaskrsnice[POCETAK][i]);
                    b.putString("TRAJANJE", podaciRaskrsnice[TRAJANJE][i]);
                    b.putString("SMER_LEVO", podaciRaskrsnice[SMER_LEVO][i].equals("1") ? strLevo : "0");
                    b.putString("SMER_PRAVO", podaciRaskrsnice[SMER_PRAVO][i].equals("1") ? strPravo : "0");
                    b.putString("SMER_DESNO", podaciRaskrsnice[SMER_DESNO][i].equals("1") ? strDesno : "0");
                    intent.putExtras(b);
                    getActivity().startActivityForResult(intent, REQ_CODE);
                }
        });
        if (greska)
            iskljuciDugme(button);
        return rootView;
    }

    private void postaviDefaultVrednost(Spinner spinner) {
        for(int i = 0; i < podaciRaskrsnice[0].length; i++)
            if(!istekloVreme(podaciRaskrsnice[DATUM][i]+" "+podaciRaskrsnice[POCETAK][i])) {
                spinner.setSelection(Integer.valueOf(i));
                break;
            }
    }

    private boolean istekloVreme(String datum) {
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy HH:mm");
        Date now = Calendar.getInstance().getTime();
        try {
            Date zadatak = sdf.parse(datum);
            if(zadatak.after(now)) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void iskljuciDugme(Button dugme) {
        Drawable mDrawable = getResources().getDrawable(R.drawable.spinner_background);
        mDrawable.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
        mDrawable = DrawableCompat.wrap(mDrawable);
        int h = mDrawable.getIntrinsicHeight();
        int w = mDrawable.getIntrinsicWidth();
        mDrawable.setBounds(0, 0, w, h);
        dugme.setBackground(mDrawable);
        dugme.setClickable(false);
    }

    private void ukljuciDugme(Button dugme) {
        dugme.setBackground(getResources().getDrawable(R.drawable.spinner_background));
        dugme.setClickable(true);
    }
}