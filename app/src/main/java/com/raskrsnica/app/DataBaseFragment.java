package com.raskrsnica.app;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataBaseFragment extends Fragment {


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
            //Todo Da se dinamicki kreira
            TextView[] tvNaziv = new TextView[2];
            TextView[] tvDatum = new TextView[2];
            TextView[] tvVreme = new TextView[2];

            tvNaziv[0] = (TextView) view.findViewById(R.id.tvTest1Naziv);
            tvDatum[0] = (TextView) view.findViewById(R.id.tvTest1Datum);
            tvVreme[0] = (TextView) view.findViewById(R.id.tvTest1Vreme);
            tvNaziv[1] = (TextView) view.findViewById(R.id.tvTest2Naziv);
            tvDatum[1] = (TextView) view.findViewById(R.id.tvTest2Datum);
            tvVreme[1] = (TextView) view.findViewById(R.id.tvTest2Vreme);

            SharedPreferences sharedPref = this.getActivity().getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);

            JSONArray merenja = new JSONArray(sharedPref.getString("MerenjaJSON", "0"));

            for (int i = 0; i < merenja.length() && i < 2;  i++) {
                JSONObject merenje = merenja.getJSONObject(i);
                tvNaziv[i].setText(merenje.getString("Naziv"));
                tvDatum[i].setText("Datum: "+merenje.getString("Datum"));
                tvVreme[i].setText("Vreme:"+merenje.getString("Vreme"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
