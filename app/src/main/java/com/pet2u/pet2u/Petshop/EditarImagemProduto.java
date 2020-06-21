package com.pet2u.pet2u.Petshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.MascaraEditUtil;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;

public class EditarImagemProduto extends AppCompatActivity {

    private EditText campoTelefone;
    private Button botao_enviar, botao_voltar;
    private DatabaseReference firebaseDatabase = Conexao.getFirebaseDatabase();
    private FirebaseAuth auth= Conexao.getFirebaseAuth();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_imagem_produto);
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
                String telefone = campoTelefone.getText().toString().trim();
                atualizarTelefone(telefone);
                alert("Telefone Alterado com sucesso!");
                startActivity(new Intent(getApplicationContext(), PerfilUsuario_Activity.class));
                finish();
            }
        });
    }

    private void mascarasNumeros(){
        campoTelefone.addTextChangedListener(MascaraEditUtil.mask(campoTelefone, MascaraEditUtil.FORMAT_CELULAR));

    }

    private void alert(String msg){
        Toast.makeText(EditarImagemProduto.this, msg, Toast.LENGTH_SHORT).show();
    }
    private void atualizarTelefone(String telefone){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Criptografia.codificar(emailUsuario);
        DatabaseReference usuarioRef = firebaseDatabase.child("Usuario").child(idUsuario);

        usuarioRef.child("telefone").setValue(telefone);

    }

    private void inicializaComponentes(){
        campoTelefone = findViewById(R.id.inputEditarTelefone);
        botao_enviar = findViewById(R.id.botao_Enviar_Redefinir_Telefone);
        botao_voltar = findViewById(R.id.botaoVoltar_alterarTelefone);
    }
}
