package com.raskrsnica.app;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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

    //globalne prom jer treba da im pristupimo na vise mesta (pri kreiranju i pri prosledjivanju na drugu aktivnost)
    Spinner spinner1, spinner2;
    TextView mesec, datum;
    TimePicker tp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        //Posto niz raskrsnica treba dinamicki da ucitavamo iz baze, ne moze da se cita iz string.xml (tu su konstante)
        //TODO Da se ovaj niz stringova ucita iz baze
        String[] raskrsnice=new String[]{"Raskrsnica1","Raskrsnica2", "Raskrsnica3", "Raskrsnica4"};

        spinner1=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, raskrsnice);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        /* TODO Ako se slazes sa prethodnim, obrisi ovaj zakomentarisan kod i obrisi niz stringova "raskrsnica" iz strings.xml
        Spinner spinner1=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 =ArrayAdapter.createFromResource(rootView.getContext(),R.array.raskrsnice, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
*/
        spinner2=(Spinner) rootView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 =ArrayAdapter.createFromResource(rootView.getContext(),R.array.pozicije, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        LinearLayout layout=(LinearLayout) rootView.findViewById(R.id.datum_layout);
        mesec=(TextView) rootView.findViewById(R.id.mesec);
        datum=(TextView)rootView.findViewById(R.id.datum);
        mesec.setText(month + "");
        datum.setText(day + "");

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        //Todo Stavio sam da je globalna promenljiva, al me buni ovo "final", obrisi ako mislis da ne mora s tim
        //final TimePicker tp = (TimePicker) rootView.findViewById(R.id.timePicker);
        tp = (TimePicker) rootView.findViewById(R.id.timePicker);
        tp.setIs24HourView(true);

        Button button=(Button)rootView.findViewById(R.id.btNext); //Kad se klikne next dugme
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),CountingActivity.class); //pravimo novi intent
                Bundle b = new Bundle(); // pravimo paket za podatke koje cemo da prosledimo
                //Ubacujemo podatke u paket u formatu <kljuc> <vrednost>
                b.putString("RASKRSNICA", spinner1.getSelectedItem().toString());
                b.putString("POZICIJA", spinner2.getSelectedItem().toString());
                b.putString("DATUM", datum.getText().toString()+" "+mesec.getText().toString()); //TODO Da se ubaci i godina!
                b.putString("VREME", tp.getCurrentHour()+ ":" + tp.getCurrentMinute());
                intent.putExtras(b); // ubacujemo podatke intentu
                startActivity(intent); // pozivamo intent
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
    INTENT
        -Prosledjivanje parametara: https://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter
    DATUM I VREME
        -ucitavanje vrednosti: https://stackoverflow.com/questions/10346685/android-how-to-get-values-from-date-and-time-picker
*/