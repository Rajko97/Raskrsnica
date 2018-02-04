package com.raskrsnica.app;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */

public class SettingsFragment extends Fragment {

    final static int REQ_CODE = 1;
    public int i=0;
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

    Spinner spinner1, spinner2;
    TextView mesec, datum, godina;
    TimePicker tp;
    CountDownTimer countDownTimer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        //TODO Da se ovaj niz stringova ucita iz baze
        String[] raskrsnice=new String[]{"Raskrsnica1","Raskrsnica2", "Raskrsnica3", "Raskrsnica4"};
        String[] pozicije=new String[]{"1","2", "3", "4"};

        spinner1=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(rootView.getContext(), R.layout.view_spinner_item, raskrsnice);
        adapter1.setDropDownViewResource(R.layout.dropdownlist_style);
        spinner1.setAdapter(adapter1);
        spinner1.setDropDownVerticalOffset(50);
        spinner1.setOnTouchListener(Spinner_OnTouch);

        /*spinner2=(Spinner) rootView.findViewById(R.id.spinner2);
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


        final TextView Levo, Pravo, Desno;
        Levo = (TextView) rootView.findViewById(R.id.textLevo);
        Pravo = (TextView) rootView.findViewById(R.id.textPravo);
        Desno = (TextView) rootView.findViewById(R.id.textDesno);

        Button button=(Button)rootView.findViewById(R.id.btNext); //Kad se klikne next dugme
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                if(!tbLevo.isChecked() && !tbDesno.isChecked() && !tbPravo.isChecked()) {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Greska!");
                    alertDialog.setMessage("Niste izabrali nijedan smer!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                    {
                        /*final AlertDialog alertDialog= new AlertDialog.Builder(getActivity()).create();
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


                        countDownTimer = new CountDownTimer(getRemainingTimeinMS(2), 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                try
                                {
                                    long mills = getRemainingTimeinMS(1);
                                    int hours = 23-(int) ((mills/(1000 * 60 * 60) % 24));
                                    int mins = 59-(int) (mills/(1000*60)) % 60;
                                    int seconds = 59-(int) (mills/1000) % 60;
                                    String diff = hours + ":" + mins+":"+seconds;
                                    alertDialog.setMessage(diff);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }*/

                            //@Override
                            //public void onFinish() {
                                //alertDialog.dismiss();
                               // PokreniBrojanje();
                            //}
                        //}.start();

            /*}

            private long getRemainingTimeinMS(int arg) {
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                Date d = new Date();
                Date date2 = null;
                try {
                    date2 = format.parse((tp.getCurrentHour() + ":" + tp.getCurrentMinute()+":00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mills = d.getTime()-date2.getTime();
                if (arg == 1)
                    return mills;
                else {
                    int hours = 23-(int) ((mills/(1000 * 60 * 60) % 24));
                    int mins = 59-(int) (mills/(1000*60)) % 60;
                    int seconds = 59-(int) (mills/1000) % 60;
                    return (long) (hours*60*60+mins*60+seconds)*1000;
                }
            }

            private void PokreniBrojanje() {
                Intent intent = new Intent(getActivity(), CountingActivity.class);
                Bundle b = new Bundle();
                b.putString("RASKRSNICA", spinner1.getSelectedItem().toString());
                b.putString("POZICIJA", spinner2.getSelectedItem().toString());
                b.putString("DATUM", datum.getText().toString() + " " + mesec.getText().toString() + " " + godina.getText().toString());
                b.putString("VREME", tp.getCurrentHour() + ":" + tp.getCurrentMinute());
                b.putString("SMER_LEVO", tbLevo.isChecked()?Levo.getText().toString():"0");
                b.putString("SMER_PRAVO", tbPravo.isChecked()?Pravo.getText().toString():"0");
                b.putString("SMER_DESNO", tbDesno.isChecked()?Desno.getText().toString():"0");
                intent.putExtras(b);
                getActivity().startActivityForResult(intent, REQ_CODE);
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
        });*/
        return rootView;

    }
    private View.OnTouchListener Spinner_OnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                spinner1.setBackgroundResource(R.drawable.spinner_background);
                Toast.makeText(getContext(), "Zatvara se lista", Toast.LENGTH_SHORT).show();
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                spinner1.setBackgroundResource(R.drawable.spinner_background2);
                Toast.makeText(getContext(), "Otvara se lista", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };


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