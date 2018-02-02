package com.raskrsnica.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Responsable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.loginbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //konekt sa bazom
                //upit da li je username pronadjen
                //ako nije, onda greska
                //ako jeste, select password from baza where username == username
                //uporedjujemo password-e
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void successResponse(JSONObject res) {
    }

    @Override
    public void errorResponse(JSONObject err) {

    }
}
