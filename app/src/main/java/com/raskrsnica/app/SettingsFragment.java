package com.raskrsnica.app;



import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

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
    SimpleDateFormat dateformat3 = new SimpleDateFormat("yyy");
    String year = dateformat3.format(c.getTime());
    ToggleButton tbLevo, tbDesno, tbPravo;

    //globalne prom jer treba da im pristupimo na vise mesta (pri kreiranju i pri prosledjivanju na drugu aktivnost)
    Spinner spinner1, spinner2;
    TextView mesec, datum, godina;
    TimePicker tp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        //TODO Da se ovaj niz stringova ucita iz baze
        String[] raskrsnice=new String[]{"Raskrsnica1","Raskrsnica2", "Raskrsnica3", "Raskrsnica4"};
        String[] pozicije=new String[]{"1","2", "3", "4"};

        spinner1=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, raskrsnice);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner2=(Spinner) rootView.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 =new ArrayAdapter<>(rootView.getContext(),android.R.layout.simple_spinner_item,pozicije);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        LinearLayout layout=(LinearLayout) rootView.findViewById(R.id.datum_layout);
        mesec=(TextView) rootView.findViewById(R.id.mesec);
        datum=(TextView)rootView.findViewById(R.id.datum);
        godina=(TextView)rootView.findViewById(R.id.godina);
        mesec.setText(month + "");
        datum.setText(day + "");
        godina.setText(year+"");


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        tp = (TimePicker) rootView.findViewById(R.id.timePicker);
        tp.setIs24HourView(true);

        tbLevo=(ToggleButton)rootView.findViewById(R.id.toggleButtonLevo);
        tbDesno=(ToggleButton)rootView.findViewById(R.id.toggleButtonDesno);
        tbPravo=(ToggleButton)rootView.findViewById(R.id.toggleButtonPravo);

        Button button=(Button)rootView.findViewById(R.id.btNext); //Kad se klikne next dugme
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tbLevo.isChecked() || tbDesno.isChecked() || tbPravo.isChecked()) { //provera da li je korisnik ukljucio bar jedan smer
                    Intent intent = new Intent(getActivity(), CountingActivity.class); //pravimo novi intent
                    Bundle b = new Bundle(); // pravimo paket za podatke koje cemo da prosledimo
                    //Ubacujemo podatke u paket u formatu <kljuc> <vrednost>
                    b.putString("RASKRSNICA", spinner1.getSelectedItem().toString());
                    b.putString("POZICIJA", spinner2.getSelectedItem().toString());
                    b.putString("DATUM", datum.getText().toString() + " " + mesec.getText().toString() + " " + godina.getText().toString());
                    b.putString("VREME", tp.getCurrentHour() + ":" + tp.getCurrentMinute());
                    //TODO da se proslede izabrani smerovi
                    intent.putExtras(b); // ubacujemo podatke intentu
                    startActivity(intent); // pozivamo intent
                }
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Kad menjamo poziciju sa koje se broji
            @Override                                                                 //moramo da promenimo i nazive smerova
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView Levo, Pravo, Desno;
                Levo = (TextView) rootView.findViewById(R.id.textLevo);
                Pravo = (TextView) rootView.findViewById(R.id.textPravo);
                Desno = (TextView) rootView.findViewById(R.id.textDesno);
                int x = Integer.valueOf(spinner2.getSelectedItem().toString());
                int y = x % 4 +1;
                Levo.setText(x+"."+y);
                y = (x +1)% 4 +1;
                Pravo.setText(x+"."+y);
                y = (x +2)% 4 +1;
                Desno.setText(x+"."+y);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }
}

/*Literatura:
    SPINER
    -Sve o spineru: https://developer.android.com/reference/android/widget/Spinner.html
    -Kreiranje spinera sa nizom stringova: https://stackoverflow.com/questions/17311335/how-to-populate-a-spinner-from-string-array
    -Citanje vrednosti: https://stackoverflow.com/questions/5787809/get-spinner-selected-items-text
    -Kad se promeni vrednost - event: https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
    INTENT
        -Prosledjivanje parametara: https://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter
    DATUM I VREME
        -ucitavanje vrednosti: https://stackoverflow.com/questions/10346685/android-how-to-get-values-from-date-and-time-picker

*/