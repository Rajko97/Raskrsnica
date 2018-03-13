package com.raskrsnica.app;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Scanner;
import java.util.concurrent.CancellationException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataBaseFragment extends Fragment {
    private static String URL_ZA_SLANJE = "https://api.myjson.com/bins/1afprx";
    RestClient restClient;

    final static int LAYOUT_ID = 500, CHECKBOX_ID = 1000;

    int brojMerenja = 0;
    LinearLayout myLayout;
    public DataBaseFragment() {
        // Required empty public constructor
    }
    int brojCekiranih = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_data_base, container, false);
        restClient = RestClient.getInstance();
        ImageButton dugmeObisi = (ImageButton) rootView.findViewById(R.id.btDelete);
        ImageButton dugmeBaza = (ImageButton) rootView.findViewById(R.id.btUpload);
        dugmeObisi.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
        dugmeBaza.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
        dugmeObisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.alert_dialog1);
                final TextView tv1 = dialog.findViewById(R.id.tv1);
                tv1.setText("Da li ste sigurni da želite da uklonite izabrana merenja?");
                final TextView tv2 = dialog.findViewById(R.id.tv2);
                tv2.setText("Ukoliko potvrdite, izabrani podaci koje ste merili biće zauvek izgubljeni iz uređaja!");
                dialog.setCancelable(false);
                Button btDa = dialog.findViewById(R.id.btDa);
                btDa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int id = 0; id < brojMerenja; id++) {
                            CheckBox checkBox = (CheckBox) rootView.findViewById(CHECKBOX_ID + id);
                            if (checkBox.isChecked()) {
                                LinearLayout ln = (LinearLayout) rootView.findViewById(LAYOUT_ID + id);
                                ln.setVisibility(View.GONE);
                                obrisiJSONelement(id);
                                checkBox.setId(0);
                                ln.setId(0);
                                for (int j = id; j < brojMerenja - 1; j++) {
                                    CheckBox checkBox1 = (CheckBox) rootView.findViewById(CHECKBOX_ID + j + 1);
                                    checkBox1.setId(CHECKBOX_ID + j);
                                    LinearLayout ln2 = (LinearLayout) rootView.findViewById(LAYOUT_ID + j + 1);
                                    ln2.setId(LAYOUT_ID + j);
                                }
                                brojMerenja--;
                                brojCekiranih--;
                                id--;
                                if (brojMerenja == 0)
                                    ispisiGresku(rootView);
                            }
                        }
                        if (brojCekiranih == 0) {
                            ImageButton dugmeObisi = (ImageButton) rootView.findViewById(R.id.btDelete);
                            ImageButton dugmeBaza = (ImageButton) rootView.findViewById(R.id.btUpload);
                            dugmeObisi.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
                            dugmeBaza.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
                            dugmeObisi.setClickable(false);
                            dugmeBaza.setClickable(false);
                        }
                        dialog.cancel();
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
            private void obrisiJSONelement(int position) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);

                try {
                    String korisnik = sharedPref.getString("UlogovanKorisnik", "");
                    JSONArray jsonArray = new JSONArray(sharedPref.getString("Merenja"+korisnik, ""));
                    JSONArray list = new JSONArray();
                    int len = jsonArray.length();
                    if(jsonArray != null) {
                        for(int i=0; i<len; i++)
                            if (i != position)
                                list.put(jsonArray.get(i));

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Merenja"+korisnik, list.toString());
                        editor.apply();
                    }
                } catch (JSONException e) {
                    ispisiGresku(rootView);
                    e.printStackTrace();
                }
            }
        });

        dugmeBaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int id = 0; id <brojMerenja; id++)
                {
                    CheckBox checkBox = (CheckBox) rootView.findViewById(CHECKBOX_ID+id);
                    if(checkBox.isChecked()) {
                        if(uploaduj(id)) {
                            LinearLayout ln = (LinearLayout) rootView.findViewById(LAYOUT_ID + id);
                            ln.setVisibility(View.GONE);
                            //ToDo Ucitaj element i pokreni metodu
                            obrisiJSONelement(id);
                            checkBox.setId(0);
                            ln.setId(0);
                            //ToDo Optimalniji nacin je da se broji koliko je bilo brisanja i da se shiftuje za toliko mesta
                            for (int j = id; j < brojMerenja - 1; j++) {
                                CheckBox checkBox1 = (CheckBox) rootView.findViewById(CHECKBOX_ID + j + 1);
                                checkBox1.setId(CHECKBOX_ID + j);
                                LinearLayout ln2 = (LinearLayout) rootView.findViewById(LAYOUT_ID + j + 1);
                                ln2.setId(LAYOUT_ID + j);
                            }
                            brojMerenja--;
                            brojCekiranih--;
                            id--;
                            if (brojMerenja == 0)
                                ispisiGresku(rootView);
                            final Dialog dialog = new Dialog(getContext());
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.alert_dialog2);
                            final TextView tv2=dialog.findViewById(R.id.tv2);
                            tv2.setText("Merenja su uspesno poslata na server.");
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
                        else {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.alert_dialog2);
                            final TextView tv1=dialog.findViewById(R.id.tv1);
                            tv1.setText("Greska!");
                            final TextView tv2=dialog.findViewById(R.id.tv2);
                            tv2.setText("Baza trenutno nije dostupna.");
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
                }
                if(brojCekiranih == 0)
                {
                    ImageButton dugmeObisi = (ImageButton) rootView.findViewById(R.id.btDelete);
                    ImageButton dugmeBaza = (ImageButton) rootView.findViewById(R.id.btUpload);
                    dugmeObisi.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
                    dugmeBaza.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
                    dugmeObisi.setClickable(false);
                    dugmeBaza.setClickable(false);
                }
            }
            private boolean uploaduj(int position) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
                try {
                    String korisnik = sharedPref.getString("UlogovanKorisnik", "");
                    JSONArray jsonArray = new JSONArray(sharedPref.getString("Merenja"+korisnik, ""));
                    JSONObject data = jsonArray.getJSONObject(position);
                    String response = restClient.postRequest(URL_ZA_SLANJE, data);
                    if (response.equals("-1"))
                        return false;

                } catch (JSONException e) {
                    ispisiGresku(rootView);
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
            private void obrisiJSONelement(int position) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);

                try {
                    String korisnik = sharedPref.getString("UlogovanKorisnik", "");
                    JSONArray jsonArray = new JSONArray(sharedPref.getString("Merenja"+korisnik, ""));
                    JSONArray list = new JSONArray();
                    int len = jsonArray.length();
                    if(jsonArray != null) {
                        for(int i=0; i<len; i++)
                            if (i != position)
                                list.put(jsonArray.get(i));

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Merenja"+korisnik, list.toString());
                        editor.apply();
                    }
                } catch (JSONException e) {
                    ispisiGresku(rootView);
                    e.printStackTrace();
                }
            }
        });

        dugmeObisi.setClickable(false);
        dugmeBaza.setClickable(false);
        loadData(rootView);

        return rootView;
    }

    private void loadData(final View view) {
        StringBuilder builder = new StringBuilder();

        try {
            SharedPreferences sharedPref = this.getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
            String korisnik = sharedPref.getString("UlogovanKorisnik", "");
            JSONArray merenja = new JSONArray(sharedPref.getString("Merenja"+korisnik, "0"));
            //odavde
            myLayout = (LinearLayout) view.findViewById(R.id.LayoutBaza);

            brojMerenja = merenja.length();
            for (int i = 0; i < brojMerenja;  i++) {
                //Pravimo novi element
                JSONObject merenje = merenja.getJSONObject(i);
                ImageView ikonica = new ImageView(getContext());
                LinearLayout glavniLayout = new LinearLayout(getContext());
                LinearLayout layoutInformacije = new LinearLayout(getContext());
                LinearLayout layoutNaziv = new LinearLayout(getContext());
                TextView tekstRaskrsnica = new TextView(getContext());
                TextView tekstNazivRaskrsnice = new TextView(getContext());
                TextView tekstDatum = new TextView(getContext());
                TextView tekstVreme = new TextView(getContext());
                LinearLayout cbLayout=new LinearLayout(getContext());
                final CheckBox checkBox = new CheckBox(getContext());

                if((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_XLARGE) ==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE){
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams myNewLayout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            height);

                    glavniLayout.setLayoutParams(myNewLayout);
                    glavniLayout.setOrientation(LinearLayout.HORIZONTAL);
                    glavniLayout.setBackgroundResource(R.drawable.background_baza_layout);
                    glavniLayout.setId(LAYOUT_ID+i);
                    myLayout.addView(glavniLayout);
                    //Ikonica na pocetku

                    LinearLayout.LayoutParams ikonicaParms = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            0.06f
                    );
                    ikonicaParms.gravity = Gravity.CENTER_VERTICAL;
                    ikonica.setLayoutParams(ikonicaParms);
                    ikonica.setImageResource(R.drawable.ic_calendar_color_x);
                    glavniLayout.addView(ikonica);
                    //LayoutInformacije
                    LinearLayout.LayoutParams layoutInformacijeParms = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0.86f
                    );

                    layoutInformacije.setLayoutParams(layoutInformacijeParms);
                    layoutInformacije.setOrientation(LinearLayout.VERTICAL);
                    glavniLayout.addView(layoutInformacije);
                    //Layout za Naziv Raskrsnice
                    LinearLayout.LayoutParams layoutNazivParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0,
                            0.33f
                    );

                    layoutNaziv.setLayoutParams(layoutNazivParms);
                    layoutNaziv.setOrientation(LinearLayout.HORIZONTAL);
                    int paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    layoutNaziv.setPaddingRelative(paddingStart, 0, 0, 0);
                    layoutNaziv.setGravity(Gravity.CENTER_VERTICAL);
                    layoutInformacije.addView(layoutNaziv);
                    //Tekst "Raskrsnica:"

                    LinearLayout.LayoutParams tekstRaskrsnicaParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tekstRaskrsnica.setLayoutParams(tekstRaskrsnicaParms);
                    tekstRaskrsnica.setGravity(Gravity.CENTER_VERTICAL);
                    tekstRaskrsnica.setText("Raskrsnica:");
                    tekstRaskrsnica.setTextColor(Color.parseColor("#D90647"));
                    tekstRaskrsnica.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                    tekstRaskrsnica.setTypeface(Typeface.DEFAULT_BOLD);
                    layoutNaziv.addView(tekstRaskrsnica);
                    //Tekst Naziv raskrsnice
                    paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams tekstNazivRaskrsniceParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tekstNazivRaskrsnice.setLayoutParams(tekstNazivRaskrsniceParms);
                    tekstNazivRaskrsnice.setGravity(Gravity.CENTER_VERTICAL);
                    tekstNazivRaskrsnice.setPaddingRelative(paddingStart, 0, 0, 0);
                    tekstNazivRaskrsnice.setText(merenje.getString("Naziv"));
                    tekstNazivRaskrsnice.setTextColor(Color.parseColor("#000000"));
                    tekstNazivRaskrsnice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                    layoutNaziv.addView(tekstNazivRaskrsnice);
                    //Tekst Datum
                    paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams tekstDatumParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0,
                            0.33f
                    );
                    tekstDatum.setLayoutParams(tekstDatumParms);
                    tekstDatum.setPaddingRelative(paddingStart, 0, 0, 0);
                    tekstDatum.setText("Datum: "+merenje.getString("Datum"));
                    tekstDatum.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    tekstDatum.setGravity(Gravity.CENTER_VERTICAL);
                    layoutInformacije.addView(tekstDatum);
                    //Tekst Vreme
                    paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams tekstVremeParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0,
                            0.33f
                    );
                    tekstVreme.setLayoutParams(tekstVremeParms);
                    tekstVreme.setPaddingRelative(paddingStart, 0, 0, 0);
                    tekstVreme.setText("Vreme: "+merenje.getString("Vreme"));
                    tekstVreme.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    tekstVreme.setGravity(Gravity.CENTER_VERTICAL);
                    layoutInformacije.addView(tekstVreme);
                    //CheckBox

                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,0.08f);
                    cbLayout.setLayoutParams(layoutParams);
                    cbLayout.setGravity( Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                    glavniLayout.addView(cbLayout);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams checkBoxParms = new LinearLayout.LayoutParams(size,size);
                    checkBox.setButtonDrawable(R.drawable.checkbox_background_xlarge);
                    checkBox.setLayoutParams(checkBoxParms);

                }else{
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                    int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams myNewLayout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,width);

                    glavniLayout.setLayoutParams(myNewLayout);
                    glavniLayout.setOrientation(LinearLayout.HORIZONTAL);
                    glavniLayout.setBackgroundResource(R.drawable.background_baza_layout);
                    glavniLayout.setId(LAYOUT_ID+i);
                    myLayout.addView(glavniLayout);
                    //Ikonica na pocetku

                    LinearLayout.LayoutParams ikonicaParms = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            0.06f
                    );
                    ikonicaParms.gravity = Gravity.CENTER_VERTICAL;
                    ikonica.setLayoutParams(ikonicaParms);
                    ikonica.setImageResource(R.drawable.ic_calendar_color);
                    glavniLayout.addView(ikonica);
                    //LayoutInformacije
                    LinearLayout.LayoutParams layoutInformacijeParms = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0.87f
                    );

                    layoutInformacije.setLayoutParams(layoutInformacijeParms);
                    layoutInformacije.setOrientation(LinearLayout.VERTICAL);
                    glavniLayout.addView(layoutInformacije);
                    //Layout za Naziv Raskrsnice
                    LinearLayout.LayoutParams layoutNazivParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0,
                            0.33f
                    );

                    layoutNaziv.setLayoutParams(layoutNazivParms);
                    layoutNaziv.setOrientation(LinearLayout.HORIZONTAL);
                    int paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    layoutNaziv.setPaddingRelative(paddingStart, 0, 0, 0);
                    layoutNaziv.setGravity(Gravity.CENTER_VERTICAL);
                    layoutInformacije.addView(layoutNaziv);
                    //Tekst "Raskrsnica:"

                    LinearLayout.LayoutParams tekstRaskrsnicaParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tekstRaskrsnica.setLayoutParams(tekstRaskrsnicaParms);
                    tekstRaskrsnica.setGravity(Gravity.CENTER_VERTICAL);
                    tekstRaskrsnica.setText("Raskrsnica:");
                    tekstRaskrsnica.setTextColor(Color.parseColor("#D90647"));
                    tekstRaskrsnica.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    tekstRaskrsnica.setTypeface(Typeface.DEFAULT_BOLD);
                    layoutNaziv.addView(tekstRaskrsnica);
                    //Tekst Naziv raskrsnice
                    paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams tekstNazivRaskrsniceParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tekstNazivRaskrsnice.setLayoutParams(tekstNazivRaskrsniceParms);
                    tekstNazivRaskrsnice.setGravity(Gravity.CENTER_VERTICAL);
                    tekstNazivRaskrsnice.setPaddingRelative(paddingStart, 0, 0, 0);
                    tekstNazivRaskrsnice.setText(merenje.getString("Naziv"));
                    tekstNazivRaskrsnice.setTextColor(Color.parseColor("#000000"));
                    tekstNazivRaskrsnice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    layoutNaziv.addView(tekstNazivRaskrsnice);
                    //Tekst Datum
                    paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams tekstDatumParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0,
                            0.33f
                    );
                    tekstDatum.setLayoutParams(tekstDatumParms);
                    tekstDatum.setPaddingRelative(paddingStart, 0, 0, 0);
                    tekstDatum.setText("Datum: "+merenje.getString("Datum"));
                    tekstDatum.setGravity(Gravity.CENTER_VERTICAL);
                    layoutInformacije.addView(tekstDatum);
                    //Tekst Vreme
                    paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams tekstVremeParms = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0,
                            0.33f
                    );
                    tekstVreme.setLayoutParams(tekstVremeParms);
                    tekstVreme.setPaddingRelative(paddingStart, 0, 0, 0);
                    tekstVreme.setText("Vreme: "+merenje.getString("Vreme"));
                    tekstVreme.setGravity(Gravity.CENTER_VERTICAL);
                    layoutInformacije.addView(tekstVreme);
                    //CheckBox

                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,0.07f);
                    cbLayout.setLayoutParams(layoutParams);
                    cbLayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                    glavniLayout.addView(cbLayout);

                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams checkBoxParms = new LinearLayout.LayoutParams(size,size);
                    checkBox.setButtonDrawable(R.drawable.checkbox_background);
                    checkBox.setLayoutParams(checkBoxParms);
                }

                checkBox.setId(CHECKBOX_ID+i);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(brojCekiranih == 0)
                        {
                            ImageButton dugmeObisi = (ImageButton) view.findViewById(R.id.btDelete);
                            ImageButton dugmeBaza = (ImageButton) view.findViewById(R.id.btUpload);
                            dugmeObisi.setColorFilter(Color.parseColor("#D90647"), PorterDuff.Mode.SRC_ATOP);
                            dugmeBaza.setColorFilter(Color.parseColor("#D90647"), PorterDuff.Mode.SRC_ATOP);
                            dugmeObisi.setClickable(true);
                            dugmeBaza.setClickable(true);

                        }
                        if (checkBox.isChecked())
                            brojCekiranih++;
                        else
                            brojCekiranih--;
                        if(brojCekiranih == 0)
                        {
                            ImageButton dugmeObisi = (ImageButton) view.findViewById(R.id.btDelete);
                            ImageButton dugmeBaza = (ImageButton) view.findViewById(R.id.btUpload);
                            dugmeObisi.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
                            dugmeBaza.setColorFilter(getResources().getColor(R.color.colorDisabledGrey), PorterDuff.Mode.SRC_ATOP);
                            dugmeObisi.setClickable(false);
                            dugmeBaza.setClickable(false);
                        }
                    }
                });
                cbLayout.addView(checkBox);
            }
            if(brojMerenja == 0)
                ispisiGresku(view);
        } catch (JSONException e) {
            ispisiGresku(view);
            e.printStackTrace();
        }

    }

    private void ispisiGresku(View view) {
        LinearLayout myLayout = (LinearLayout) view.findViewById(R.id.layoutBazaGlavni);
        TextView tekstGreska = new TextView(getContext());
        LinearLayout.LayoutParams tekstGreskaParms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        tekstGreska.setLayoutParams(tekstGreskaParms);
        tekstGreska.setText("Ooops! Nismo pronašli ništa u bazu.");
        tekstGreska.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tekstGreska.setTextColor(Color.parseColor("#B9B9B9"));
        myLayout.addView(tekstGreska);

    }

}
