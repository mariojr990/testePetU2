package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet_4 extends AppCompatActivity {

    private ImageView imagemCategoriaPetCadastrado4;
    private Spinner spinnerMeuPetCadastro4;
    private Button BotaoProximoCadmeupet4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_4);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
    }

    private void clicks() {

        BotaoProximoCadmeupet4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeCategoriaPet = getIntent().getExtras().getString("nomeCategoriaPet");
                if (!nomeCategoriaPet.equals("Peixe")){
                    String racaPet = spinnerMeuPetCadastro4.getSelectedItem().toString();

                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_5.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                    intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
                    intent.putExtra("racaPet", racaPet);
                    startActivity(intent);
                }else{
                    String racaPet = spinnerMeuPetCadastro4.getSelectedItem().toString();
                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_6.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                    intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
                    intent.putExtra("racaPet", racaPet);
                    startActivity(intent);
                }
            }
        });

    }

    private void inicializarComponentes() {
        imagemCategoriaPetCadastrado4 = findViewById(R.id.imagemCategoriaPetCadastrado4);
        spinnerMeuPetCadastro4 = findViewById(R.id.spinnerMeuPetCadastro4);
        BotaoProximoCadmeupet4 = findViewById(R.id.BotaoProximoCadmeupet4);

        String nomeCategoriaPet = getIntent().getExtras().getString("nomeCategoriaPet");

        verificarImagem(nomeCategoriaPet);
    }

    private void verificarImagem(String categoria){
        switch (categoria){
            case "Cachorro":
                imagemCategoriaPetCadastrado4.setImageResource(R.drawable.dog_icon);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter
                        .createFromResource(this,
                                R.array.raca_Cachorro,
                                android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMeuPetCadastro4.setAdapter(adapter);
                break;

            case "Gato":
                imagemCategoriaPetCadastrado4.setImageResource(R.drawable.cat_icon);
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter
                        .createFromResource(this,
                                R.array.raca_gato,
                                android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMeuPetCadastro4.setAdapter(adapter2);
                break;

            case "Pássaro":
                imagemCategoriaPetCadastrado4.setImageResource(R.drawable.bird_icon);
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter
                        .createFromResource(this,
                                R.array.raca_passaro,
                                android.R.layout.simple_spinner_item);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMeuPetCadastro4.setAdapter(adapter3);
                break;

            case "Roedor":
                imagemCategoriaPetCadastrado4.setImageResource(R.drawable.rodent_icon);
                ArrayAdapter<CharSequence> adapter4 = ArrayAdapter
                        .createFromResource(this,
                                R.array.raca_roedor,
                                android.R.layout.simple_spinner_item);
                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMeuPetCadastro4.setAdapter(adapter4);
                break;

            case "Réptil":
                imagemCategoriaPetCadastrado4.setImageResource(R.drawable.reptil_icon);
                ArrayAdapter<CharSequence> adapter5 = ArrayAdapter
                        .createFromResource(this,
                                R.array.raca_reptil,
                                android.R.layout.simple_spinner_item);
                adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMeuPetCadastro4.setAdapter(adapter5);
                break;

            case "Peixe":
                imagemCategoriaPetCadastrado4.setImageResource(R.drawable.fish_icon);
                ArrayAdapter<CharSequence> adapter6 = ArrayAdapter
                        .createFromResource(this,
                                R.array.raca_peixe,
                                android.R.layout.simple_spinner_item);
                adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMeuPetCadastro4.setAdapter(adapter6);
                break;
        }

    }


}