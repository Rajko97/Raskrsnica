package com.raskrsnica.app;



import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsFragment extends Fragment implements CustomSpinner.OnSpinnerEventsListener{

    private static String url_servera = "http://www.rajko.esy.es/Raskrsnice/";

    final static int REQ_CODE = 1, NAZIV = 10, BR_MESTO = 1, DATUM = 2, POCETAK = 3, TRAJANJE = 4, SMER_LEVO = 5,
            SMER_PRAVO = 6, SMER_DESNO = 7, SLIKA = 8, ZADATAK_ID = 9, ZADATAK_NAZIV = 0;
    
    public SettingsFragment() {
        // Required empty public constructor
    }

    CustomSpinner spinner;
    CountDownTimer countDownTimer;
    String strLevo, strPravo, strDesno;
    String[][] podaciRaskrsnice;
    Boolean greska = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        try {
            JSONArray zadaci = new JSONArray(sharedPref.getString("Zadaci", ""));
            if(zadaci.length()>0) {
                podaciRaskrsnice = new String[11][zadaci.length()];
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
                    podaciRaskrsnice[SLIKA][i] = url_servera+zadatak.getString("Slika");
                    //podaciRaskrsnice[SLIKA][i] = zadatak.getString("Slika");
                    podaciRaskrsnice[ZADATAK_ID][i] = zadatak.getString("id");
                    podaciRaskrsnice[ZADATAK_NAZIV][i] = zadatak.getString("Zadatak");
                }
            } else {
                greska = true;
            }
        } catch (JSONException e) {
            greska = true;
            e.printStackTrace();
        }
        if (greska){
            podaciRaskrsnice = new String[1][1];
            podaciRaskrsnice[0][0] = "Nemate nijedan zadatak";
        }
        spinner=(CustomSpinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(rootView.getContext(), R.layout.view_spinner_item, podaciRaskrsnice[ZADATAK_NAZIV]) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
               View v = null;
               v = super.getDropDownView(position, null, parent);
               if(position == podaciRaskrsnice[0].length-1) {
                   v.setBackgroundResource(R.drawable.spiner_last_item_backgrounds);
               }
               return v;
            }
        };
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

                    ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView2);
                    Picasso.with(getContext())
                            .load(podaciRaskrsnice[SLIKA][i])
                            .placeholder(R.drawable.img_w)
                            .into(imageView);

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
                }
            });
            spinner.setSpinnerEventsListener(this);
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
                        PokreniBrojanje();
                    }
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
                    b.putString("ZADATAK_ID", podaciRaskrsnice[ZADATAK_ID][i]);
                    intent.putExtras(b);
                    getActivity().startActivityForResult(intent, REQ_CODE);
                }
        });
        if (greska)
            iskljuciDugme(button);
        return rootView;
    }

    //defaut vrednost je prvi zadatak koji nailazi
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

    @Override
    public void onSpinnerOpened() {
        spinner.setBackgroundResource(R.drawable.spinner_background2);
    }

    @Override
    public void onSpinnerClosed() {
        spinner.setBackgroundResource(R.drawable.spinner_background);
    }
}