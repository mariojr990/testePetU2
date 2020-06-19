package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.MascaraEditUtil;
import com.pet2u.pet2u.R;


public class Cad_do_Pet_Activity extends AppCompatActivity {

    private EditText campoNome, campoRazaoSocial, campoEmail, campoCNPJ, campoTelefone, data_funcionamento, horario_funcionamento;

    private Button botao_proximo, botao_voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_do_pet);
        getSupportActionBar().hide();
        inicializaComponentes();
        Clicks();
        mascarasNumeros();

    }

    private void Clicks() {
        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        botao_proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = campoNome.getText().toString().trim();
                String razaoSocial = campoRazaoSocial.getText().toString().trim();
                String email = campoEmail.getText().toString().trim().toLowerCase();
                String cnpj = campoCNPJ.getText().toString().trim();
                String telefone = campoTelefone.getText().toString().trim();
                String data_funcionamentoo = data_funcionamento.getText().toString().trim();
                String horario_funcionamentoo = horario_funcionamento.getText().toString().trim();

                if ( nome.isEmpty() || razaoSocial.isEmpty() || email.isEmpty() || cnpj.isEmpty() || telefone.isEmpty()){
                    alert("Preencha todos os campos!");
                }else{

                    Intent cadPetshop1 = new Intent(Cad_do_Pet_Activity.this, Cad_do_Pet2_Activity.class);
                    cadPetshop1.putExtra("nomePetshop", nome);
                    cadPetshop1.putExtra("razaoSocial", razaoSocial);
                    cadPetshop1.putExtra("emailPetshop", email);
                    cadPetshop1.putExtra("cnpjPetshop", cnpj);
                    cadPetshop1.putExtra("telefonePetshop", telefone);
                    cadPetshop1.putExtra("dataFuncionamentoPetshop", data_funcionamentoo);
                    cadPetshop1.putExtra("horarioFuncionamentoPetshop", horario_funcionamentoo);
                    startActivity(cadPetshop1);

                }
            }
        });


    }


    private void alert(String msg){
        Toast.makeText(Cad_do_Pet_Activity.this, msg,Toast.LENGTH_SHORT).show();
    }

    private void mascarasNumeros(){
        campoCNPJ.addTextChangedListener(MascaraEditUtil.mask(campoCNPJ, MascaraEditUtil.FORMAT_CNPJ));
        campoTelefone.addTextChangedListener(MascaraEditUtil.mask(campoTelefone, MascaraEditUtil.FORMAT_CELULAR));

    }

    private void inicializaComponentes(){
        campoNome = findViewById(R.id.inputNomePetshop);
        campoRazaoSocial = findViewById(R.id.inputRazaoSocialPetshop2);
        campoCNPJ = findViewById(R.id.inputCnpjPetshop);
        campoTelefone = findViewById(R.id.inputTelefonePetshop);
        campoEmail = findViewById(R.id.inputEmail_Petshop);
        data_funcionamento = findViewById(R.id.data_funcionamento);
        horario_funcionamento = findViewById(R.id.horario_funcionamento);

        botao_proximo = findViewById(R.id.botaoProximoPetshop);
        botao_voltar = findViewById(R.id.botaoVoltarCadPetshop);


    }

}
