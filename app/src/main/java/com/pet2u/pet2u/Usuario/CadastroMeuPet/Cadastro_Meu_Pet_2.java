package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pet2u.pet2u.Petshop.Cad_do_Pet2_Activity;
import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet_2 extends AppCompatActivity {

    private ImageView imagemCategoriaPetCadastrado;
    private Button BotaoProximoCadmeupet2;
    private TextView txtMensagemPrincipal;
    private EditText inputNomePet_cadastroPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_2);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
    }

    private void clicks() {
        BotaoProximoCadmeupet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePet = inputNomePet_cadastroPet.getText().toString().trim();
                if(!nomePet.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_3.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomePet", nomePet);
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    startActivity(intent);
                }else{
                    alert("Epa! você esqueceu de digitar o nome do seu bichinho!!");
                }

            }
        });

    }


    private void inicializarComponentes() {
        imagemCategoriaPetCadastrado = findViewById(R.id.imagemCategoriaPetCadastrado);
        txtMensagemPrincipal = findViewById(R.id.txtMensagemPrincipal);
        inputNomePet_cadastroPet = findViewById(R.id.inputNomePet_cadastroPet);
        BotaoProximoCadmeupet2 = findViewById(R.id.BotaoProximoCadmeupet2);

        String categoria = getIntent().getExtras().getString("nomeCategoriaPet");
        txtMensagemPrincipal.setText("Eba!! Você está cadastrando um "+ categoria + " para a sua família Pet2U!");

        verificarImagem(categoria);
    }

    private void verificarImagem(String categoria){
        switch (categoria){
            case "Cachorro":
                imagemCategoriaPetCadastrado.setImageResource(R.drawable.dog_icon);
                break;

            case "Gato":
                imagemCategoriaPetCadastrado.setImageResource(R.drawable.cat_icon);
                break;

            case "Pássaro":
                imagemCategoriaPetCadastrado.setImageResource(R.drawable.bird_icon);
                break;

            case "Roedor":
                imagemCategoriaPetCadastrado.setImageResource(R.drawable.rodent_icon);
                break;

            case "Réptil":
                imagemCategoriaPetCadastrado.setImageResource(R.drawable.reptil_icon);
                break;

            case "Peixe":
                imagemCategoriaPetCadastrado.setImageResource(R.drawable.fish_icon);
                break;
        }

    }

    private void alert(String msg) {
        Toast.makeText(Cadastro_Meu_Pet_2.this, msg,Toast.LENGTH_SHORT).show();
    }
}