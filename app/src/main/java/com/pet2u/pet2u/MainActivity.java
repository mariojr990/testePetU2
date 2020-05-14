package com.pet2u.pet2u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button botao_entrar, entrarcomfacebook, botao_criar_conta;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    private FirebaseAuth auth, autenticacaopetshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClicks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }

    private void eventoClicks() {
        botao_criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CadUsuario1Activity.class);
                startActivity(i);
            }
        });
        
        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if ( !email.isEmpty() && !senha.isEmpty()) {

                    login(email, senha);

                }else
                {
                    Toast.makeText(MainActivity.this,
                            "Preencha os campos!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void redefinirSenha(View view){
        Intent intent = new Intent(this, EsqueceuSenhaActivity.class);
        startActivity(intent);
        campoEmail.setText("");
        campoSenha.setText("");
    }

    private void login(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(MainActivity.this, PerfilUsuarioActivity.class);
                            startActivity(i);
                            campoEmail.setText("");
                            campoSenha.setText("");
                        }else{
                            alert("E-mail ou senha errados");
                        }
                    }
                });
    }

    private void alert(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes(){
        botao_entrar = findViewById(R.id.EntrarLogin);
        entrarcomfacebook = findViewById(R.id.EntrarComFacebook);
        campoEmail = findViewById(R.id.EmailLoginUsuario);
        campoSenha = findViewById(R.id.SenhaLoginUsuario);
        tipoAcesso = findViewById(R.id.UsuarioPetshop);
        botao_criar_conta = findViewById(R.id.CriarNovaConta);

    }
}
