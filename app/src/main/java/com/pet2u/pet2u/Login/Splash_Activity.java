package com.pet2u.pet2u.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pet2u.pet2u.R;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirLogin();
            }
        }, 3000);

    }

    private void abrirLogin(){
        //Intent i = new Intent(Splash_Activity.this, MainActivity.class);
        Intent i = new Intent(Splash_Activity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
