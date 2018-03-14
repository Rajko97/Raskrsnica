package com.raskrsnica.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    JSONArray korisnici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        ucitajBazu();
        final TextView tv=(TextView)findViewById(R.id.tvError);
        TextView tv2=(TextView)findViewById(R.id.tv);
        ImageView img=(ImageView)findViewById(R.id.imageView);
        Button button = (Button) findViewById(R.id.loginbutton);
        final EditText username = (EditText) findViewById(R.id.etKorisnickoIme);
        final EditText password = (EditText) findViewById(R.id.etSifra);
        tv.setVisibility(View.INVISIBLE);

        Animation a= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        img.startAnimation(a);
        tv2.startAnimation(a);


        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.anim_downtoup);
        username.setAnimation(downtoup);
        password.setAnimation(downtoup);
        button.setAnimation(downtoup);

        Animation animation1=new TranslateAnimation(Animation.ABSOLUTE,Animation.ABSOLUTE,900,Animation.ABSOLUTE);
        animation1.setDuration(600);
        animation1.setFillAfter(true);
        animation1.setZAdjustment(Animation.ZORDER_TOP);
        Animation animation2=new TranslateAnimation(Animation.ABSOLUTE,Animation.ABSOLUTE,900,Animation.ABSOLUTE);
        animation2.setStartOffset(300);
        animation2.setDuration(600);
        animation2.setFillAfter(true);
        animation2.setZAdjustment(Animation.ZORDER_TOP);
        Animation animation3=new TranslateAnimation(Animation.ABSOLUTE,Animation.ABSOLUTE,900,Animation.ABSOLUTE);
        animation3.setStartOffset(600);
        animation3.setDuration(600);
        animation3.setFillAfter(true);
        animation3.setZAdjustment(Animation.ZORDER_TOP);
        /*username.startAnimation(animation1);
        password.startAnimation(animation2);
        button.startAnimation(animation3);*/

        final Animation animation5=AnimationUtils.loadAnimation(this,R.anim.fade_out);
        animation5.setStartOffset(2500);
        final Animation animation4=new TranslateAnimation(Animation.ABSOLUTE,Animation.ABSOLUTE,Animation.ABSOLUTE,50);
        animation4.setDuration(500);
        animation4.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                tv.startAnimation(animation5);
            }


        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < korisnici.length(); i++) {
                    try {
                        JSONObject korisnik = new JSONObject(korisnici.get(i).toString());
                        if (korisnik.getString("username").equals(username.getText().toString())) {
                            if(korisnik.getString("password").equals(password.getText().toString())) {
                                SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("UlogovanKorisnik", username.getText().toString());
                                editor.apply();
                                sortirajZadatke("Zadaci"+korisnik.getString("username"));
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.transition_out, R.anim.transition_in);
                                finish();
                                break;
                            }
                            else
                                tv.setText("Netačna lozinka!");
                                tv.setBackgroundColor(Color.rgb(255,255,255));
                                tv.startAnimation(animation4);
                                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning,0,0,0);

                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(i == korisnici.length()-1) {
                        tv.setText("Korisničko ime nije pronađeno");
                        tv.setBackgroundColor(Color.rgb(255,255,255));
                        tv.startAnimation(animation4);
                        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning,0,0,0);

                    }
                }
            }
        });
    }

    private void sortirajZadatke(String bazaID) {
        SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        try {
            JSONArray zadaci = new JSONArray(sharedPref.getString(bazaID, ""));
            SharedPreferences.Editor editor = sharedPref.edit();
            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i <zadaci.length(); i++)
                jsonValues.add(zadaci.getJSONObject(i));

            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                private static final String KEY_NAME = "Datum";
                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (valA.equals(valB)) {
                        try {
                            valA = a.getString("Vreme");
                            valB = b.getString("Vreme");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        try {
                            Date dateA = format.parse(valA);
                            Date dateB = format.parse(valB);
                            return dateA.compareTo(dateB);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                    SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy");
                    Calendar calA = Calendar.getInstance();
                    Calendar calB = Calendar.getInstance();
                    try {
                        calA.setTime(format.parse(valA));
                        calB.setTime(format.parse(valB));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return calA.compareTo(calB);
                }
            });
            for (int i = 0; i < zadaci.length(); i++)
                sortedJsonArray.put(jsonValues.get(i));
            editor.putString(bazaID, sortedJsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ucitajBazu() {
        SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        try {
            korisnici = new JSONArray(sharedPref.getString("Korisnici", ""));
        } catch (JSONException e) {
            final Dialog dialog = new Dialog(LoginActivity.this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog2);
            final TextView tv1=dialog.findViewById(R.id.tv1);
            tv1.setText("Greška!");
            final TextView tv2=dialog.findViewById(R.id.tv2);
            tv2.setText("Aplikaciji je potrebno da uspostavi vezu sa bazom!\n" +
                    "Proverite vašu internet konekciju. Ako još uvek imate problema, kontaktirajte profesora!");
            dialog.setCancelable(false);
            Button btOk=dialog.findViewById(R.id.btOk);
            btOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginActivity.this.finish();
                }
            });
            dialog.show();
        }
    }
}
