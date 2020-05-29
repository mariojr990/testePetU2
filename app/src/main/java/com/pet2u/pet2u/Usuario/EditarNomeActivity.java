package com.pet2u.pet2u.Usuario;

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
        import com.pet2u.pet2u.R;

public class EditarNomeActivity extends AppCompatActivity {
    private Button botao_enviar_nome,botao_voltar_nome;
    private EditText campoNome;
    private DatabaseReference firebaseDatabase = Conexao.getFirebaseDatabase();
    private FirebaseAuth auth= Conexao.getFirebaseAuth();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_nome);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClick();
    }

    private void eventoClick() {
        botao_voltar_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botao_enviar_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = campoNome.getText().toString().trim();
                atualizarTelefone(nome);
                alert("Nome Alterado com sucesso!");
                startActivity(new Intent(getApplicationContext(), PerfilUsuario_Activity.class));
                finish();
            }
        });
    }

    private void alert(String msg){
        Toast.makeText(EditarNomeActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    private void atualizarTelefone(String nome){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Criptografia.codificar(emailUsuario);
        DatabaseReference usuarioRef = firebaseDatabase.child("Usuario").child(idUsuario);

        usuarioRef.child("nome").setValue(nome);

    }

    private void inicializaComponentes(){
    botao_enviar_nome = findViewById(R.id.botao_Enviar_Redefinir_Nome);
    botao_voltar_nome= findViewById(R.id.botao_voltar_redefinir_Nome);
    campoNome = findViewById(R.id.inputEditarNome);
    }
}
