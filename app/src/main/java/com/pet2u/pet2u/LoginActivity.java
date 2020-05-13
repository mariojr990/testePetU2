package com.pet2u.pet2u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    private Button botao_entrar;
    //private Button cadastrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        botao_entrar = findViewById(R.id.EntrarLogin);
        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PerfilUsuarioActivity.class);
                startActivity(intent);
            }
        });

//        cadastrarUsuario = findViewById(R.id.CriarNovaConta);
//        cadastrarUsuario.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent (getApplicationContext(), MainActivity.class);
//                startActivity(intent2);
//            }
//        });
    }
}
