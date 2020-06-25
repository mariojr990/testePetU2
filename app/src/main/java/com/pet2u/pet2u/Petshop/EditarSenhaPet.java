package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.R;

public class EditarSenhaPet extends AppCompatActivity {
    private EditText campoEmailPet;
    private Button botao_enviar, botao_voltar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_senha_pet);
        inicializaComponentes();
        eventoClick();
    }
    private void eventoClick() {
        botao_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmailPet.getText().toString().trim();
                resetSenha(email);
            }
        });

        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetSenha(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(EditarSenhaPet.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            alert("Um e-mail para alteração da senha foi enviado para o seu email");
                            finish();
                        }else{
                            alert("E-mail não registrado");
                        }
                    }
                });
    }

    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();

    }

    private void inicializaComponentes(){
        campoEmailPet = findViewById(R.id.inputEditarEmailPet);
        botao_enviar = findViewById(R.id.botao_EnviarPet);
        botao_voltar = findViewById(R.id.botaoVoltar_redefinirSenha);
    }
}