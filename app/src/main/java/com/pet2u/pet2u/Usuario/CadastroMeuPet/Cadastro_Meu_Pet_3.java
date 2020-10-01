package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet_3 extends AppCompatActivity {

    private ImageView imagemCategoriaPetCadastrado3;
    private TextView txtMensagemPrincipal3, txtPeguntaBichinho3, imageMenino, imageMenina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_3);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
    }

    private void clicks() {
        imageMenino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_4.class);
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                intent.putExtra("GeneroPet", "Macho");
                startActivity(intent);

            }
        });

        imageMenina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_4.class);
                intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                intent.putExtra("GeneroPet", "Fêmea");
                startActivity(intent);
            }
        });

    }


    private void inicializarComponentes() {
        imagemCategoriaPetCadastrado3 = findViewById(R.id.imagemCategoriaPetCadastrado3);
        txtMensagemPrincipal3 = findViewById(R.id.txtMensagemPrincipal3);
        txtPeguntaBichinho3 = findViewById(R.id.txtPeguntaBichinho3);
        imageMenino = findViewById(R.id.imageMenino);
        imageMenina = findViewById(R.id.imageMenina);

        String nomePet = getIntent().getExtras().getString("nomePet");
        txtPeguntaBichinho3.setText(nomePet + " é um menino ou uma menina?");


        String categoria = getIntent().getExtras().getString("nomeCategoriaPet");
        verificarImagem(categoria);

    }

    private void verificarImagem(String categoria){
        switch (categoria){
            case "Cachorro":
                imagemCategoriaPetCadastrado3.setImageResource(R.drawable.dog_icon);
                break;

            case "Gato":
                imagemCategoriaPetCadastrado3.setImageResource(R.drawable.cat_icon);
                break;

            case "Pássaro":
                imagemCategoriaPetCadastrado3.setImageResource(R.drawable.bird_icon);
                break;

            case "Roedor":
                imagemCategoriaPetCadastrado3.setImageResource(R.drawable.rodent_icon);
                break;

            case "Réptil":
                imagemCategoriaPetCadastrado3.setImageResource(R.drawable.reptil_icon);
                break;

            case "Peixe":
                imagemCategoriaPetCadastrado3.setImageResource(R.drawable.fish_icon);
                break;
        }

    }
}