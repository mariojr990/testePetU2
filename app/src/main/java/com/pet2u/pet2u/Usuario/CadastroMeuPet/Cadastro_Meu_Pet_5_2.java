package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet_5_2 extends AppCompatActivity {

    private TextView txtMensagemPrincipal5_2, txtPeguntaAnoNascimento2;
    private Button buttonProximo_5_2;
    private ImageView imagemCategoriaPetCadastrado5_2;
    private Spinner spinnerMeuPetAnoNascimento2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_5_2);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
    }

    private void clicks() {

        buttonProximo_5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String anoNascimento = spinnerMeuPetAnoNascimento2.getSelectedItem().toString();

                if(anoNascimento != ""){

                    Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_6.class);
                    intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
                    intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
                    intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
                    intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
                    intent.putExtra("racaPet",  getIntent().getExtras().getString("racaPet"));
                    intent.putExtra("anoNascimentoPet",  anoNascimento);
                    intent.putExtra("castradoPet",  "naoPossui");
                    startActivity(intent);
                }else{
                    alert("Opa! parece que você não selecionou o ano de nascimento do seu Pet!");
                }
            }
        });

    }

    private void inicializarComponentes() {

        txtMensagemPrincipal5_2 = findViewById(R.id.txtMensagemPrincipal5_2);
        txtPeguntaAnoNascimento2 = findViewById(R.id.txtPeguntaAnoNascimento2);
        imagemCategoriaPetCadastrado5_2 = findViewById(R.id.imagemCategoriaPetCadastrado5_2);
        spinnerMeuPetAnoNascimento2 = findViewById(R.id.spinnerMeuPetAnoNascimento2);
        buttonProximo_5_2 = findViewById(R.id.buttonProximo_5_2);

        txtMensagemPrincipal5_2.setText("Legal " + getIntent().getExtras().getString("nomeUsuario") + ", seu bichinho é um " + getIntent().getExtras().getString("racaPet") +"!!");
        txtPeguntaAnoNascimento2.setText("Em qual ano " + getIntent().getExtras().getString("nomePet") + " nasceu?");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.anoNascimentoAnimal,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeuPetAnoNascimento2.setAdapter(adapter);



        String nomeCategoriaPet = getIntent().getExtras().getString("nomeCategoriaPet");
        verificarImagem(nomeCategoriaPet);
    }

    private void verificarImagem(String categoria){
        switch (categoria){
            case "Cachorro":
                imagemCategoriaPetCadastrado5_2.setImageResource(R.drawable.dog_icon);
                break;

            case "Gato":
                imagemCategoriaPetCadastrado5_2.setImageResource(R.drawable.cat_icon);
                break;

            case "Pássaro":
                imagemCategoriaPetCadastrado5_2.setImageResource(R.drawable.bird_icon);
                break;

            case "Roedor":
                imagemCategoriaPetCadastrado5_2.setImageResource(R.drawable.rodent_icon);
                break;

            case "Réptil":
                imagemCategoriaPetCadastrado5_2.setImageResource(R.drawable.reptil_icon);
                break;

            case "Peixe":
                imagemCategoriaPetCadastrado5_2.setImageResource(R.drawable.fish_icon);
                break;
        }
    }
    private void alert(String msg) {
        Toast.makeText(Cadastro_Meu_Pet_5_2.this, msg,Toast.LENGTH_SHORT).show();
    }

}