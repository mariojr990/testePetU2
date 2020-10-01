package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet_5 extends AppCompatActivity {

    private TextView txtMensagemPrincipal5, txtPeguntaAnoNascimento, txtPeguntaCastrado, buttonThumbsUpCastrado, buttonThumbsDownCastrado;
    private ImageView imagemCategoriaPetCadastrado5;
    private Spinner spinnerMeuPetAnoNascimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_5);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
    }

    private void clicks() {

        buttonThumbsUpCastrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String anoNascimento = spinnerMeuPetAnoNascimento.getSelectedItem().toString();

                if(anoNascimento != ""){

                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_6.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                    intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
                    intent.putExtra("racaPet",  getIntent().getExtras().getString("racaPet"));
                    intent.putExtra("anoNascimentoPet",  anoNascimento);
                    intent.putExtra("castradoPet",  "sim");
                    startActivity(intent);
                }else{
                    alert("Opa! parece que você não selecionou o ano de nascimento do seu Pet!");
                }

            }
        });

        buttonThumbsDownCastrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String anoNascimento = spinnerMeuPetAnoNascimento.getSelectedItem().toString();

                if(anoNascimento != ""){

                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_6.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                    intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
                    intent.putExtra("racaPet",  getIntent().getExtras().getString("racaPet"));
                    intent.putExtra("anoNascimentoPet",  anoNascimento);
                    intent.putExtra("castradoPet",  "não");
                    startActivity(intent);
                }else{
                    alert("Opa! parece que você não selecionou o ano de nascimento do seu Pet!");
                }

            }
        });

    }

    private void inicializarComponentes() {

        txtMensagemPrincipal5 = findViewById(R.id.txtMensagemPrincipal5);
        txtPeguntaAnoNascimento = findViewById(R.id.txtPeguntaAnoNascimento);
        txtPeguntaCastrado = findViewById(R.id.txtPeguntaCastrado);
        buttonThumbsUpCastrado = findViewById(R.id.buttonThumbsUpCastrado);
        buttonThumbsDownCastrado = findViewById(R.id.buttonThumbsDownCastrado);
        imagemCategoriaPetCadastrado5 = findViewById(R.id.imagemCategoriaPetCadastrado5);
        spinnerMeuPetAnoNascimento = findViewById(R.id.spinnerMeuPetAnoNascimento);

        txtMensagemPrincipal5.setText("Legal " + getIntent().getExtras().getString("nomeUsuario") + ", seu bichinho é um " + getIntent().getExtras().getString("racaPet") +"!!");
        txtPeguntaAnoNascimento.setText("Em qual ano " + getIntent().getExtras().getString("nomePet") + " nasceu?");

        String genero = getIntent().getExtras().getString("GeneroPet");
        if(genero.equals("Macho")){
            txtPeguntaCastrado.setText(getIntent().getExtras().getString("nomePet") + " é castrado?");
        }else{
            txtPeguntaCastrado.setText(getIntent().getExtras().getString("nomePet") + " é castrada?");
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.anoNascimentoAnimal,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeuPetAnoNascimento.setAdapter(adapter);



        String nomeCategoriaPet = getIntent().getExtras().getString("nomeCategoriaPet");
        verificarImagem(nomeCategoriaPet);
    }

    private void verificarImagem(String categoria){
        switch (categoria){
            case "Cachorro":
            imagemCategoriaPetCadastrado5.setImageResource(R.drawable.dog_icon);
            break;

            case "Gato":
            imagemCategoriaPetCadastrado5.setImageResource(R.drawable.cat_icon);
            break;

            case "Pássaro":
            imagemCategoriaPetCadastrado5.setImageResource(R.drawable.bird_icon);
            break;

            case "Roedor":
            imagemCategoriaPetCadastrado5.setImageResource(R.drawable.rodent_icon);
            break;

            case "Réptil":
            imagemCategoriaPetCadastrado5.setImageResource(R.drawable.reptil_icon);
            break;

            case "Peixe":
            imagemCategoriaPetCadastrado5.setImageResource(R.drawable.fish_icon);
            break;
        }
    }
    private void alert(String msg) {
        Toast.makeText(Cadastro_Meu_Pet_5.this, msg,Toast.LENGTH_SHORT).show();
    }

}