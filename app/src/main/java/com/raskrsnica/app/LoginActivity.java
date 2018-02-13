package com.raskrsnica.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

    JSONArray korisnici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        ucitajBazu();
        
        Button button = (Button) findViewById(R.id.loginbutton);
        final EditText username = (EditText) findViewById(R.id.etKorisnickoIme);
        final EditText password = (EditText) findViewById(R.id.etSifra);

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
                                finish();
                            }
                            else
                                Toast.makeText(LoginActivity.this, "Netačna lozinka!", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(i == korisnici.length()-1)
                        Toast.makeText(LoginActivity.this, "Korisničko ime nije pronađeno", Toast.LENGTH_SHORT).show();
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
            korisnici = new JSONArray(sharedPref.getString("Korisnici", "[]"));
        } catch (JSONException e) {
            new android.app.AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Greška!")
                    .setMessage("Aplikaciji je potrebno da uspostavi vezu sa bazom!\n" +
                            "Proverite vašu internet konekciju. Ako još uvek imate problema, kontaktirajte profesora!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LoginActivity.this.finish();
                        }
                    })
                    .show();
        }
    }
}
