package com.pet2u.pet2u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button botao_entrar, cadastrarUsuario, entrarcomfacebook;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        inicializaComponentes();

        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if ( !email.isEmpty()) {
                    if ( !senha.isEmpty()) {

                        //Verifica estado do switch
                        if ( tipoAcesso.isChecked()) {//Login de usuário



                        }else {//Login de PetShop

                        }

                    }else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha o senha, por favor!",
                                Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o e-mail, por favor!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        botao_entrar = findViewById(R.id.EntrarLogin);
        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PerfilPet.class);
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

    private void inicializaComponentes(){
        botao_entrar = findViewById(R.id.EntrarLogin);
        cadastrarUsuario = findViewById(R.id.CriarNovaConta);
        entrarcomfacebook = findViewById(R.id.EntrarComFacebook);
        campoEmail = findViewById(R.id.EmailLoginUsuario);
        campoSenha = findViewById(R.id.SenhaLoginUsuario);
        tipoAcesso = findViewById(R.id.UsuarioPetshop);

    }
}
