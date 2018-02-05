package com.raskrsnica.app;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class DataBaseFragment extends Fragment {

    LinearLayout myLayout;
    public DataBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_base, container, false);
        loadData(rootView);
        return rootView;
    }

    private void loadData(View view) {
        StringBuilder builder = new StringBuilder();

        try {
            SharedPreferences sharedPref = this.getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);

            JSONArray merenja = new JSONArray(sharedPref.getString("MerenjaJSON", "0"));
            //odavde
            myLayout = (LinearLayout) view.findViewById(R.id.LayoutBaza);

            for (int i = 0; i < merenja.length();  i++) {
                //Pravimo novi element
                JSONObject merenje = merenja.getJSONObject(i);

                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams myNewLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);
                LinearLayout glavniLayout = new LinearLayout(getContext());
                glavniLayout.setLayoutParams(myNewLayout);
                glavniLayout.setPadding(0, 0,  padding, 0);
                glavniLayout.setBackgroundResource(R.drawable.background_baza_layout);
                myLayout.addView(glavniLayout);
                //Ikonica na pocetku
                ImageView ikonica = new ImageView(getContext());
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams ikonicaParms = new LinearLayout.LayoutParams(
                        width,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                ikonicaParms.weight =  0.05f;
                ikonicaParms.gravity = Gravity.CENTER_VERTICAL;
                ikonica.setLayoutParams(ikonicaParms);
                ikonica.setImageResource(R.drawable.ic_calendar_color);
                glavniLayout.addView(ikonica);
                //LayoutInformacije
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams layoutInformacijeParms = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                layoutInformacijeParms.weight = 0.90f;
                LinearLayout layoutInformacije = new LinearLayout(getContext());
                layoutInformacije.setLayoutParams(layoutInformacijeParms);
                layoutInformacije.setOrientation(LinearLayout.VERTICAL);
                glavniLayout.addView(layoutInformacije);
                //Layout za Naziv Raskrsnice
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams layoutNazivParms = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        width
                );
                layoutNazivParms.weight = 0.33f;
                LinearLayout layoutNaziv = new LinearLayout(getContext());
                layoutNaziv.setLayoutParams(layoutNazivParms);
                layoutNaziv.setOrientation(LinearLayout.HORIZONTAL);
                int paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                layoutNaziv.setPaddingRelative(paddingStart, 0, 0, 0);
                layoutNaziv.setGravity(Gravity.CENTER_VERTICAL);
                layoutInformacije.addView(layoutNaziv);
                //Tekst "Raskrsnica:"
                TextView tekstRaskrsnica = new TextView(getContext());
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
                TextView tekstNazivRaskrsnice = new TextView(getContext());
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
                TextView tekstDatum = new TextView(getContext());
                LinearLayout.LayoutParams tekstDatumParms = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                tekstDatumParms.weight = 0.33f;
                tekstDatum.setPaddingRelative(paddingStart, 0, 0, 0);
                tekstDatum.setText("Datum: "+merenje.getString("Datum"));
                tekstDatum.setLayoutParams(tekstNazivRaskrsniceParms);
                tekstDatum.setGravity(Gravity.CENTER_VERTICAL);
                layoutInformacije.addView(tekstDatum);
                //Tekst Vreme
                paddingStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                TextView tekstVreme = new TextView(getContext());
                LinearLayout.LayoutParams tekstVremeParms = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                tekstVremeParms.weight = 0.33f;
                tekstVreme.setPaddingRelative(paddingStart, 0, 0, 0);
                tekstVreme.setText("Vreme: "+merenje.getString("Vreme"));
                tekstVreme.setLayoutParams(tekstNazivRaskrsniceParms);
                tekstVreme.setGravity(Gravity.CENTER_VERTICAL);
                layoutInformacije.addView(tekstVreme);
                //CheckBox
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                CheckBox checkBox = new CheckBox(getContext());
                LinearLayout.LayoutParams checkBoxParms = new LinearLayout.LayoutParams(
                        width,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                checkBoxParms.weight = 0.05f;
                checkBoxParms.gravity = Gravity.CENTER_VERTICAL;
                checkBox.setLayoutParams(checkBoxParms);
                glavniLayout.addView(checkBox);


                //tvNaziv[i].setText(merenje.getString("Naziv"));
                //tvDatum[i].setText("Datum: "+merenje.getString("Datum"));
                //tvVreme[i].setText("Vreme:"+merenje.getString("Vreme"));

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
