package com.raskrsnica.app;



import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }
    Calendar c = Calendar.getInstance();
    SimpleDateFormat dateformat1 = new SimpleDateFormat("MMMM");
    String month = dateformat1.format(c.getTime());
    SimpleDateFormat dateformat2 = new SimpleDateFormat("dd");
    String day = dateformat2.format(c.getTime());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Spinner spinner1=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 =ArrayAdapter.createFromResource(rootView.getContext(),R.array.raskrsnice, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner2=(Spinner) rootView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 =ArrayAdapter.createFromResource(rootView.getContext(),R.array.pozicije, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        LinearLayout layout=(LinearLayout) rootView.findViewById(R.id.datum_layout);
        TextView mesec=(TextView) rootView.findViewById(R.id.mesec);
        TextView datum=(TextView)rootView.findViewById(R.id.datum);
        mesec.setText(month + "");
        datum.setText(day + "");

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        return rootView;
    }


}
