package com.pet2u.pet2u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet2u.pet2u.modelo.Usuario;

public class CadUsuario1Activity extends AppCompatActivity {

    private Button botao_cadastro, botao_voltar;
    private EditText campoNome, campoEmail, campoSenha, campoCPF, campoTelefone;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_usuario1);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClicks();

    }

    private void eventoClicks() {
        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botao_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString().trim();
                String nome = campoNome.getText().toString().trim();
                String senha = campoSenha.getText().toString().trim();
                String cpf = campoCPF.getText().toString().trim();
                String telefone = campoTelefone.getText().toString().trim();
                criarUser(email, senha);

                Usuario usu = new Usuario();

                usu.setCampoNome(nome);
                usu.setCampoEmail(email);
                usu.setCampoSenha(senha);
                usu.setCampoCPF(cpf);
                usu.setCampoTelefone(telefone);
                databaseReference.child("Usuario").child(usu.getCampoCPF()).setValue(usu);
                limparCampos();

            }
        });
    }

    private void limparCampos() {
        campoEmail.setText("");
        campoNome.setText("");
        campoSenha.setText("");
        campoCPF.setText(null);
        campoTelefone.setText(null);
    }

    private void criarUser(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadUsuario1Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            alert("Usuário cadastrado com sucesso!");
                            Intent i = new Intent(CadUsuario1Activity.this, PerfilUsuarioActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            alert("Erro ao efetuar o cadastro");
                        }
                    }
                });
    }

    private void alert(String msg){
        Toast.makeText(CadUsuario1Activity.this, msg, Toast.LENGTH_SHORT).show();
    }


    private void inicializaComponentes(){
        campoNome = findViewById(R.id.inputNome);
        campoCPF = findViewById(R.id.inputCPF);
        campoTelefone = findViewById(R.id.inputTelefone);
        campoEmail = findViewById(R.id.inputEmail);
        campoSenha = findViewById(R.id.inputSenha);
        botao_cadastro = findViewById(R.id.botaoCadastrarrUsuario);
        botao_voltar = findViewById(R.id.botaoVoltar);

        FirebaseApp.initializeApp(CadUsuario1Activity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }


    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }



}
