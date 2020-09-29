package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet_6 extends AppCompatActivity {

    private TextView botaoAdicionarDepois_CadMeuPet6, txtMensagemPrincipal6;
    private ImageView imagemCategoriaPetCadastrado6;
    private ImageButton buttonImagemDoBichinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_6);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
    }

    private void clicks() {
        botaoAdicionarDepois_CadMeuPet6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String categoria = getIntent().getExtras().getString("nomeCategoriaPet");
                if(categoria.equals("Peixe")){
                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_7.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                    intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
                    intent.putExtra("racaPet", getIntent().getExtras().getString("racaPet"));
                    startActivity(intent);
                }else{

                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_7.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                    intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
                    intent.putExtra("racaPet", getIntent().getExtras().getString("racaPet"));
                    intent.putExtra("anoNascimentoPet",  getIntent().getExtras().getString("anoNascimentoPet"));
                    intent.putExtra("castradoPet", getIntent().getExtras().getString("castradoPet"));
                    startActivity(intent);
                }


            }
        });


    }

    private void inicializarComponentes() {
        botaoAdicionarDepois_CadMeuPet6 = findViewById(R.id.botaoAdicionarDepois_CadMeuPet6);
        imagemCategoriaPetCadastrado6 = findViewById(R.id.imagemCategoriaPetCadastrado6);
        buttonImagemDoBichinho = findViewById(R.id.buttonImagemDoBichinho);
        txtMensagemPrincipal6 = findViewById(R.id.txtMensagemPrincipal6);


        txtMensagemPrincipal6.setText(getIntent().getExtras().getString("nomeUsuario")+ ", adicione uma foto do seu bichinho e deixe o perfil do " + getIntent().getExtras().getString("nomePet") + " ainda mais completo!");

        String nomeCategoriaPet = getIntent().getExtras().getString("nomeCategoriaPet");
        verificarImagem(nomeCategoriaPet);
    }

    private void verificarImagem(String categoria){
        switch (categoria){
            case "Cachorro":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.dog_icon);
                break;

            case "Gato":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.cat_icon);
                break;

            case "Pássaro":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.bird_icon);
                break;

            case "Roedor":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.rodent_icon);
                break;

            case "Réptil":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.reptil_icon);
                break;

            case "Peixe":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.fish_icon);
                break;
        }
    }
}