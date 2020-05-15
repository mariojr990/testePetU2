package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.R;

public class PerfilPet1Activity extends AppCompatActivity {
    private Switch tipoCadastro;
    private Button logout, tipoCadastroProduto,tipoCadastroServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet1);
        getSupportActionBar().hide();

        inicializaComponentes();

        tipoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipoCadastro.isChecked()){//CADASTRAR NOVO PRODUTO
                    tipoCadastroProduto.setVisibility(View.VISIBLE);
                    tipoCadastroServico.setVisibility(View.GONE);

                    tipoCadastroProduto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), CadastroProdutoActivity.class));
                        }
                    });

                }else{//CADASTRAR NOVO SERVIÇO
                    tipoCadastroServico.setVisibility(View.VISIBLE);
                    tipoCadastroProduto.setVisibility(View.GONE);
                    tipoCadastroServico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), CadastroServicoActivity.class));
                        }
                    });
                }
            }
        });
//        public void onClick(View view) {
//
//        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logOut();
                finish();
            }
        });
    }


    public void inicializaComponentes(){
        tipoCadastro = findViewById(R.id.tipoCadastro);
        logout = findViewById(R.id.sairLogout);
        tipoCadastroProduto = findViewById(R.id.tipoCadastroProduto);
        tipoCadastroServico = findViewById(R.id.tipoCadastroServico);

    }
}
