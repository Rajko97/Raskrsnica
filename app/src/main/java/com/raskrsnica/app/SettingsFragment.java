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
import android.view.ViewTreeObserver;
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
    CountDownTimer countDownTimer;

    boolean OtvorenSpiner = false;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        //TODO Da se ovaj niz stringova ucita iz baze
        String[] raskrsnice=new String[]{"Raskrsnica1","Raskrsnica2", "Raskrsnica3", "Raskrsnica4"};
        String[] pozicije=new String[]{"1","2", "3", "4"};

        spinner1=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(rootView.getContext(), R.layout.view_spinner_item, raskrsnice);
        adapter1.setDropDownViewResource(R.layout.dropdownlist_style);
        spinner1.setAdapter(adapter1);
        //spinner1.setDropDownVerticalOffset(50);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (OtvorenSpiner)
                    spinner1.setBackgroundResource(R.drawable.spinner_background);
                OtvorenSpiner=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(OtvorenSpiner)
                    spinner1.setBackgroundResource(R.drawable.spinner_background);
                OtvorenSpiner=false;
            }
        });

        spinner1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(OtvorenSpiner) {
                        spinner1.setBackgroundResource(R.drawable.spinner_background);
                        OtvorenSpiner = false;
                    }
                    else {
                        spinner1.setBackgroundResource(R.drawable.spinner_background2);
                        OtvorenSpiner = true;
                    }
                }
                return false;
            }
        });

        spinner1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                spinner1.setDropDownVerticalOffset(spinner1.getDropDownVerticalOffset() + spinner1.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    spinner1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    spinner1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });




        Button button=(Button)rootView.findViewById(R.id.btNext);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
               /* if (spinner1.getSelectedItem().equals(null)) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Greska!")
                            .setMessage("Niste izabrali raskrsnicu!")
                            .setPositiveButton("OK", null)
                            .show();
                } else {*/
                   /* final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
                            try {
                                long mills = getRemainingTimeinMS(1);
                                int hours = 23 - (int) ((mills / (1000 * 60 * 60) % 24));
                                int mins = 59 - (int) (mills / (1000 * 60)) % 60;
                                int seconds = 59 - (int) (mills / 1000) % 60;
                                String diff = hours + ":" + mins + ":" + seconds;
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
                    }.start();*/
                   PokreniBrojanje();

                //}
            }

            private long getRemainingTimeinMS(int arg) {
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                Date d = new Date();
                Date date2 = null;
                try {
                    //date2 = format.parse((tp.getCurrentHour() + ":" + tp.getCurrentMinute()+":00"));
                    date2 = format.parse(("24" + ":" + "60 "+":00"));
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
                b.putString("POZICIJA", "1");
                //b.putString("DATUM", + "datum.getText().toString()  " + mesec.getText().toString() + " " + godina.getText().toString());
                //b.putString("VREME", tp.getCurrentHour() + ":" + tp.getCurrentMinute());
                b.putString("SMER_LEVO", "1");
                b.putString("SMER_PRAVO", "1");
                b.putString("SMER_DESNO", "0");
                intent.putExtras(b);
                getActivity().startActivityForResult(intent, REQ_CODE);
            }
            private void PostaviIndekseSmerova(int pozicija) {
                String Levo, Pravo, Desno;
                Levo = pozicija+"."+pozicija % 4 +1;
                Pravo = pozicija+"."+(pozicija +1)% 4 +1;
                Desno = pozicija+"."+(pozicija +2)% 4 +1;
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