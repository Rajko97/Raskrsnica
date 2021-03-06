package com.raskrsnica.app;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static String RUTA_ZA_CHECK_LOGIN  = "http://192.168.0.109:8000/api/login";
    //private static String RUTA_ZA_CHECK_LOGIN  = "http://www.rajko.esy.es/Raskrsnice/RUTALOGIN.txt";


    JSONArray zadaciKorisnika = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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


        Animation animation1=new TranslateAnimation(Animation.ABSOLUTE,Animation.ABSOLUTE,900,Animation.ABSOLUTE);
        animation1.setStartOffset(100);
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
        username.startAnimation(animation1);
        password.startAnimation(animation2);
        button.startAnimation(animation3);

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
                int greska = 0;
                String strUsername = username.getText().toString();
                String strPassword = password.getText().toString();
                if(strUsername.equals("") ||strUsername.toString().isEmpty()) {
                    greska+=1;
                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strUsername).matches())
                    greska+=4;
                if(strPassword.equals("") || strPassword.isEmpty()) {
                    greska+=2;
                }
                else if(strPassword.indexOf(' ') != -1) {
                    greska+=8;
                }

                if(greska != 0) {
                    switch (greska) {
                        case 1:
                            ispisiGresku("Unesite email adresu!");
                            break;
                        case 2:
                            ispisiGresku("Unesite lozinku!");
                            break;
                        case 3:
                            ispisiGresku("Unesite email adresu i lozinku!");
                            break;
                        case 4:
                            ispisiGresku("Email adresa nije važeća");
                            break;
                        case 6:
                            ispisiGresku("Email adresa nije važeća i niste uneli lozinku");
                            break;
                        case 8:
                            ispisiGresku("Lozinka ne sme da sadrži razmak!");
                            break;
                        case 9:
                            ispisiGresku("Unesite email adresu i lozinka ne sme da sadrzi razmak!");
                            break;
                        case 12:
                            ispisiGresku("Email adresa nije važeća i lozinka ne sme da sadrži razmak!");
                            break;
                    }
                    return;
                }

                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.waitting_dialog);
                ProgressBar pb = (ProgressBar) dialog.findViewById(R.id.progressBar2);
                dialog.show();
                pb.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

                JSONObject loginInfo = new JSONObject();
                try {
                    loginInfo.put("email", username.getText().toString());
                    loginInfo.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final JsonObjectRequest customRequest = new JsonObjectRequest(Request.Method.POST, RUTA_ZA_CHECK_LOGIN, loginInfo, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {  //uspesan login
                                try {
                                    JSONObject jsonEntity = response.getJSONObject("entity");

                                    SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("UlogovanKorisnik", jsonEntity.getString("indeks"));
                                    editor.putString("SecurityToken", jsonEntity.getString("token"));
                                    editor.apply();

                                    JSONArray zadaciInfo = jsonEntity.getJSONArray("assignments");
                                    int n = zadaciInfo.length();
                                    if (n > 0)
                                    {
                                        for (int i = 0; i < n; i++) {
                                            JSONObject zadatakInfo = new JSONObject(zadaciInfo.get(i).toString());
                                            JSONObject noviZadatakInfo = new JSONObject();

                                            String primam[] = {"id", "Naziv_Raskrsnice", "mesto", "Trajanje",
                                            "SmerLevo", "SmerPravo", "SmerDesno", "slika", "Naziv"};

                                            int tip[] = {1, 0, 0, 0, 1, 1, 1, 0, 0};

                                            String cuvam[] = {"id", "Raskrsnica", "BrMesto", "Trajanje",
                                                    "SmerLevo", "SmerPravo", "SmerDesno", "Slika", "Zadatak"};

                                            for (int j = 0; j < cuvam.length; j++) {
                                                if(tip[j] > 0)
                                                    noviZadatakInfo.put(cuvam[j], ""+zadatakInfo.getInt(primam[j]));
                                                else
                                                    noviZadatakInfo.put(cuvam[j], zadatakInfo.getString(primam[j]));
                                            }

                                            SimpleDateFormat primamDatum = new SimpleDateFormat("yyyy-mm-dd");
                                            SimpleDateFormat cuvamDatum = new SimpleDateFormat("d.m.yyyy");
                                            SimpleDateFormat primamVreme = new SimpleDateFormat("HH:mm:ss");
                                            SimpleDateFormat cuvamVreme = new SimpleDateFormat("HH:mm");

                                            Date datum = primamDatum.parse(zadatakInfo.getString("Datum"));
                                            Date vreme = primamVreme.parse(zadatakInfo.getString("Vreme"));

                                            noviZadatakInfo.put("Datum", cuvamDatum.format(datum));
                                            noviZadatakInfo.put("Vreme", cuvamVreme.format(vreme));
                                            //noviZadatakInfo.put("Slika", jsoniSlike.getString(i));

                                            zadaciKorisnika.put(noviZadatakInfo);
                                        }
                                    }
                                    else
                                        sacuvajZadatke();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                sacuvajZadatke();
                                dialog.dismiss();
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {  //neuspesan login
                                dialog.dismiss();
                                NetworkResponse response = error.networkResponse;

                                if(error instanceof TimeoutError || error instanceof NoConnectionError) {
                                   ispisiGresku("Nemate internet konekciju!");
                                }
                                else if(response != null && response.data != null) {
                                    switch (response.statusCode) {
                                        case 400:
                                           try {
                                                JSONObject podaci = new JSONObject(response.data.toString());
                                                //tv.setText(podaci.getString("message"));
                                               ispisiGresku("Greška na serveru!");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            ispisiGresku("Korisnicko ime i nalog se ne podudaraju");
                                            break;
                                        case 404:
                                          /*  try {
                                                JSONObject podaci = new JSONObject(response.data.toString());
                                                tv.setText(podaci.getString("message"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }*/
                                            ispisiGresku("Korisnik nije pronadjen");
                                            break;
                                        default:
                                            ispisiGresku("Greška na serveru");
                                            break;
                                    }
                                }
                            }
                        });
                queue.add(customRequest);
            }
            void ispisiGresku(String greska) {
                tv.setText(greska);
                tv.setBackgroundColor(Color.rgb(255, 255, 255));
                tv.startAnimation(animation4);
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0);
            }
        });
    }
    private void sacuvajZadatke() {
        SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Zadaci", zadaciKorisnika.toString());
        editor.apply();
        sortirajZadatke();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_out, R.anim.transition_in);
        finish();
    }
    private void sortirajZadatke() {
        SharedPreferences sharedPref = getSharedPreferences("Raskrsnica", Context.MODE_PRIVATE);
        try {
            JSONArray zadaci = new JSONArray(sharedPref.getString("Zadaci", ""));
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
            editor.putString("Zadaci", sortedJsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
