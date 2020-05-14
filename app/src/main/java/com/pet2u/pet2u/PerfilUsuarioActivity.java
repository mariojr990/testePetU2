package com.pet2u.pet2u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pet2u.pet2u.modelo.Usuario;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PerfilUsuarioActivity extends AppCompatActivity {
    private TextView nomeCompleto, telefone, email, senha;
    private Button botao_logout;

    private FirebaseAuth auth;
    private FirebaseUser user;
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference;
//
//    private List<Usuario> listusuario = new ArrayList<Usuario>();
//    private ArrayAdapter<Usuario> arrayAdapterUsuario;

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
        user = Conexao.getFirebaseUser();
        verificaUser();
    }

    private void verificaUser() {
//        Usuario usu = new Usuario();
//        Query query;
//        String emaill = user.getEmail();
        if(user == null){
            finish();
        }else{
//            query = databaseReference.child("Usuario").equalTo(emaill);
            email.setText(user.getEmail());
            nomeCompleto.setText(user.getUid());
        }
    }

    private void inicializaComponentes(){
        nomeCompleto = findViewById(R.id.nomeCompletoUsuario);
        telefone = findViewById(R.id.editarTelefone);
        email = findViewById(R.id.editarEmail);
        senha = findViewById(R.id.editarSenha);
        botao_logout = findViewById(R.id.botao_logout);

//        FirebaseApp.initializeApp(PerfilUsuarioActivity.this);
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference();
    }

}
