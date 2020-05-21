package com.pet2u.pet2u.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Petshop.ListagemPetshop_Activity;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Usuario;

public class PerfilUsuario_Activity extends AppCompatActivity {
    private TextView nomeCompleto, telefone, email, senha;
    private Button botao_logout, botao_alterarSenha, botao_alterarTelefone, button_vet_pets;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClick();
    }

    private void eventoClick() {
        botao_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logOut();
                finish();
            }
        });

        botao_alterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EsqueceuSenha_Activity.class);
                startActivity(i);
            }
        });

        botao_alterarTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditarTelefone_aActivity.class);
                startActivity(i);
            }
        });
        button_vet_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListagemPetshop_Activity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        user = auth.getCurrentUser();
        verificaUser();

    }

    private void verificaUser() {
        if(user == null){
            finish();
        }else{

            //CRIA A LIGAÇÃO ENTRE O USUÁRIO LOGADO E O DATABASE DELE
            String emailUsuario = user.getEmail();
            String idUsuario = Criptografia.codificar(emailUsuario);
            DatabaseReference usuarioRef = databaseReference.child("Usuario").child(idUsuario);

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    nomeCompleto.setText(usuario.getNome());
                    email.setText(usuario.getEmail());
                    telefone.setText(usuario.getTelefone());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void inicializaComponentes(){
        nomeCompleto = findViewById(R.id.nomeCompletoUsuario);
        telefone = findViewById(R.id.editarTelefone);
        email = findViewById(R.id.editarEmail);
        senha = findViewById(R.id.editarSenha);
        botao_logout = findViewById(R.id.botao_logout);
        botao_alterarSenha = findViewById(R.id.botaoAlterarSenha);
        botao_alterarTelefone = findViewById(R.id.botaoAlterarTelefone);
        button_vet_pets = findViewById(R.id.button_vet_pets);
    }

}
