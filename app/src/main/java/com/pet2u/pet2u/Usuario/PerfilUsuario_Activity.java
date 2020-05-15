package com.pet2u.pet2u.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.pet2u.pet2u.ConexaoDB.Firebase;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Usuario;

public class PerfilUsuario_Activity extends AppCompatActivity {
    private TextView nomeCompleto, telefone, email, senha;
    private Button botao_logout;

    private String nomeRecuperado, teleRecuperado, emailRecuperado;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        user = auth.getCurrentUser();
        verificaUser();
        //recuperarDados();

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
                    nomeRecuperado = usuario.getNome();
                    emailRecuperado = usuario.getEmail();
                    teleRecuperado = usuario.getTelefone();

                    nomeCompleto.setText(usuario.getNome());
                    email.setText(usuario.getEmail());
                    telefone.setText(usuario.getTelefone());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            email.setText(emailRecuperado);
//            nomeCompleto.setText(nomeRecuperado);
//            telefone.setText(teleRecuperado);
        }
    }

//    public void recuperarDados(){
//
//    }

    private void inicializaComponentes(){
        nomeCompleto = findViewById(R.id.nomeCompletoUsuario);
        telefone = findViewById(R.id.editarTelefone);
        email = findViewById(R.id.editarEmail);
        senha = findViewById(R.id.editarSenha);
        botao_logout = findViewById(R.id.botao_logout);

    }

}
