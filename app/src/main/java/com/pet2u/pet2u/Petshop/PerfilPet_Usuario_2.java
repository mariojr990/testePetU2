package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;

public class PerfilPet_Usuario_2 extends AppCompatActivity {

    private TextView nome_petshop_perfil, enderecoPetshop2, numeroPetshop2, horarioPetshop2;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet__usuario_2);
        getSupportActionBar().hide();
        inicializaComponenetes();

        nome_petshop_perfil.setText(getIntent().getExtras().getString("nomePetshop"));
        enderecoPetshop2.setText(getIntent().getExtras().getString("enderecoPetshop"));
        numeroPetshop2.setText(getIntent().getExtras().getString("telefonePetshop"));

    }

    public void voltar(View view){
        finish();
    }






    private void inicializaComponenetes(){
        nome_petshop_perfil = findViewById(R.id.nome_petshop_perfil);
        enderecoPetshop2 = findViewById(R.id.enderecoPetshop2);
        numeroPetshop2 = findViewById(R.id.numeroPetshop2);
        horarioPetshop2 = findViewById(R.id.horarioPetshop2);
        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        user = auth.getCurrentUser();
    }
}
