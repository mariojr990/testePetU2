package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet extends AppCompatActivity {

    private TextView TxtPrincipal, OpcaoCachorro, OpcaoGato, OpcaoPassaro, OpcaoRoedor, OpcaoReptil, OpcaoPeixe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet);
        getSupportActionBar().hide();
        inicializarComponentes();
        Clicks();

        String nome = getIntent().getExtras().getString("nomeUsuario");
        TxtPrincipal.setText("Olá " + nome + ", qual a espécie do seu pet?");

    }



    private void Clicks() {
        OpcaoCachorro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_2.class);
                intent.putExtra("nomeCategoriaPet", "Cachorro");
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                startActivity(intent);
            }
        });

        OpcaoGato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_2.class);
                intent.putExtra("nomeCategoriaPet", "Gato");
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                startActivity(intent);
            }
        });

        OpcaoPassaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_2.class);
                intent.putExtra("nomeCategoriaPet", "Pássaro");
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                startActivity(intent);
            }
        });

        OpcaoRoedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_2.class);
                intent.putExtra("nomeCategoriaPet", "Roedor");
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                startActivity(intent);
            }
        });

        OpcaoReptil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_2.class);
                intent.putExtra("nomeCategoriaPet", "Réptil");
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                startActivity(intent);
            }
        });

        OpcaoPeixe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_2.class);
                intent.putExtra("nomeCategoriaPet", "Peixe");
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                startActivity(intent);
            }
        });
    }

    private void inicializarComponentes() {
        TxtPrincipal = findViewById(R.id.TxtPrincipal);
        OpcaoCachorro = findViewById(R.id.OpcaoCachorro);
        OpcaoGato = findViewById(R.id.OpcaoGato);
        OpcaoPassaro = findViewById(R.id.OpcaoPassaro);
        OpcaoRoedor = findViewById(R.id.OpcaoRoedor);
        OpcaoReptil = findViewById(R.id.OpcaoReptil);
        OpcaoPeixe = findViewById(R.id.OpcaoPeixe);
    }
}