package com.raskrsnica.app;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

    final static int REQ_CODE = 1, NAZIV = 0, BR_MESTO = 1, DATUM = 2, POCETAK = 3, TRAJANJE = 4, SMER_LEVO = 5, SMER_PRAVO = 6, SMER_DESNO = 7;
    
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
    

    Spinner spinner;
    CountDownTimer countDownTimer;

    boolean OtvorenSpiner = false;
    
    String strLevo, strPravo, strDesno;
    String[][] podaciRaskrsnice;
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
            e.printStackTrace();
        }

       /*
        final String[][] podaciRaskrsnice=new String[][]{
                {"Raskrsnica1","Raskrsnica2", "Raskrsnica3", "Raskrsnica4"},
                {"2", "3", "2", "1"},
                {"5.2.2018", "6.2.2018", "7.2.2018", "8.2.2018"},
                {"12:00", "13:00", "6:00", "23:00"},
                {"2", "1", "2", "3"},
                {"1", "0", "1", "1"},
                {"1", "1", "0", "1"},
                {"1", "1", "1", "0"},
        };*/

        spinner=(Spinner) rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(rootView.getContext(), R.layout.view_spinner_item, podaciRaskrsnice[0]);
        adapter.setDropDownViewResource(R.layout.dropdownlist_style);
        spinner.setAdapter(adapter);

        final TextView tvNaziv, tvBrMesto, tvSmerovi, tvDatum, tvPocetak, tvTrajanje;
        tvNaziv = (TextView) rootView.findViewById(R.id.tvNaziv);
        tvBrMesto = (TextView) rootView.findViewById(R.id.tvBrMesto);
        tvSmerovi = (TextView) rootView.findViewById(R.id.tvSmerovi);
        tvDatum = (TextView) rootView.findViewById(R.id.tvDatum);
        tvPocetak = (TextView) rootView.findViewById(R.id.tvPocetak);
        tvTrajanje = (TextView) rootView.findViewById(R.id.tvTrajanje);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               tvNaziv.setText(podaciRaskrsnice[NAZIV][i]);
               tvBrMesto.setText(podaciRaskrsnice[BR_MESTO][i]);
               tvDatum.setText(podaciRaskrsnice[DATUM][i]);
               tvPocetak.setText(podaciRaskrsnice[POCETAK][i]);
               tvTrajanje.setText(podaciRaskrsnice[TRAJANJE][i]+"h");
               PostaviIndekseSmerova(Integer.parseInt(podaciRaskrsnice[BR_MESTO][i]));
               tvSmerovi.setText((podaciRaskrsnice[SMER_LEVO][i].equals("1")? "Levo("+strLevo+") ":"")
                       +(podaciRaskrsnice[SMER_PRAVO][i].equals("1")?"Pravo("+strPravo+") ":"")
                       +(podaciRaskrsnice[SMER_DESNO][i].equals("1")?"Desno("+strDesno+") ":""));

                if (OtvorenSpiner)
                    spinner.setBackgroundResource(R.drawable.spinner_background);
                OtvorenSpiner=false;
            }

            private void PostaviIndekseSmerova(int pozicija) {
                strLevo = pozicija+"."+(pozicija % 4 +1);
                strPravo = pozicija+"."+((pozicija +1)% 4 +1);
                strDesno = pozicija+"."+((pozicija +2)% 4 +1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(OtvorenSpiner)
                    spinner.setBackgroundResource(R.drawable.spinner_background);
                OtvorenSpiner=false;
            }
        });

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(OtvorenSpiner) {
                        spinner.setBackgroundResource(R.drawable.spinner_background);
                        OtvorenSpiner = false;
                    }
                    else {
                        spinner.setBackgroundResource(R.drawable.spinner_background2);
                        OtvorenSpiner = true;
                    }
                }
                return false;
            }
        });

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
                int i = spinner.getSelectedItemPosition();
                b.putString("RASKRSNICA", podaciRaskrsnice[NAZIV][i]);
                b.putString("POZICIJA", podaciRaskrsnice[BR_MESTO][i]);
                b.putString("DATUM", podaciRaskrsnice[DATUM][i]);
                b.putString("VREME", podaciRaskrsnice[POCETAK][i]);
                b.putString("TRAJANJE", podaciRaskrsnice[TRAJANJE][i]);
                b.putString("SMER_LEVO", podaciRaskrsnice[SMER_LEVO][i].equals("1")?strLevo:"0");
                b.putString("SMER_PRAVO", podaciRaskrsnice[SMER_PRAVO][i].equals("1")?strPravo:"0");
                b.putString("SMER_DESNO", podaciRaskrsnice[SMER_DESNO][i].equals("1")?strDesno:"0");
                intent.putExtras(b);
                getActivity().startActivityForResult(intent, REQ_CODE);
            }
        });
        return rootView;
    }
}