package com.pet2u.pet2u.Sobre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;

public class Sobre extends AppCompatActivity {
    private Button botaoVoltarSobre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        getSupportActionBar().hide();
        botaoVoltarSobre = findViewById(R.id.botaoVoltarSobre);
        click();
    }

    private void click() {
        botaoVoltarSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
