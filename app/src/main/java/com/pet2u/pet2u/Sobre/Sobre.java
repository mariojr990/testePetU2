package com.pet2u.pet2u.Sobre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;

public class Sobre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
    }

    public void botaoVoltarUsu(View view){
        Intent intent = new Intent(this, PerfilUsuario_Activity.class);
        startActivity(intent);
        finish();
    }

}
