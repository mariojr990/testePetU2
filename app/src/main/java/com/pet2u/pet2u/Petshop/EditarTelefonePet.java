package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.MascaraEditUtil;
import com.pet2u.pet2u.R;

public class EditarTelefonePet extends AppCompatActivity {
    private EditText campoTelefonePet;
    private Button botao_enviar, botao_voltar;
    private DatabaseReference firebaseDatabase = Conexao.getFirebaseDatabase();
    private FirebaseAuth auth= Conexao.getFirebaseAuth();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_telefone_pet);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClick();
        mascarasNumeros();
    }
    private void eventoClick() {
        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botao_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telefone = campoTelefonePet.getText().toString().trim();
                atualizarTelefone(telefone);
                alert("Telefone Alterado com sucesso!");
                startActivity(new Intent(getApplicationContext(), PerfilPet_petshop.class));
                finish();
            }
        });
    }

    private void mascarasNumeros(){
        campoTelefonePet.addTextChangedListener(MascaraEditUtil.mask(campoTelefonePet, MascaraEditUtil.FORMAT_CELULAR));

    }

    private void alert(String msg){
        Toast.makeText(EditarTelefonePet.this, msg, Toast.LENGTH_SHORT).show();
    }
    private void atualizarTelefone(String telefone){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Criptografia.codificar(emailUsuario);
        DatabaseReference usuarioRef = firebaseDatabase.child("Petshop").child(idUsuario);

        usuarioRef.child("telefone").setValue(telefone);

    }

    private void inicializaComponentes(){
        campoTelefonePet = findViewById(R.id.inputEditarTelefonePet);
        botao_enviar = findViewById(R.id.botao_Enviar_Redefinir_TelefonePet);
        botao_voltar = findViewById(R.id.botaoVoltar_alterarTelefone);
    }
}