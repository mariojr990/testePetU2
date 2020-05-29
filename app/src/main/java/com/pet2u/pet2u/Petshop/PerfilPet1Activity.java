package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Login.MainActivity;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;
import com.pet2u.pet2u.modelo.Usuario;

public class PerfilPet1Activity extends AppCompatActivity {
    private Switch tipoCadastro;
    private Button logout, tipoCadastroProduto,tipoCadastroServico;
    private TextView descricao_petshop, nome_petshop, email_petshop_perfil;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet1);
        getSupportActionBar().hide();
        inicializaComponentes();
        verificaUser();
        eventoClicks();

    }

    private void eventoClicks() {
        tipoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipoCadastro.isChecked()){//CADASTRAR NOVO PRODUTO
                    tipoCadastroProduto.setVisibility(View.VISIBLE);
                    tipoCadastroServico.setVisibility(View.INVISIBLE);

                    tipoCadastroProduto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), CadastroProdutoActivity.class));
                        }
                    });

                }else{//CADASTRAR NOVO SERVIÇO
                    tipoCadastroServico.setVisibility(View.VISIBLE);
                    tipoCadastroProduto.setVisibility(View.INVISIBLE);
                    tipoCadastroServico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), CadastroServicoActivity.class));
                        }
                    });
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void verificaUser() {
        if(user == null){
            finish();
        }else{

            //CRIA A LIGAÇÃO ENTRE O USUÁRIO LOGADO E O DATABASE DELE
            String emailUsuario = user.getEmail();
            String idUsuario = Criptografia.codificar(emailUsuario);
            DatabaseReference usuarioRef = databaseReference.child("Petshop").child(idUsuario);

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Petshop petshop = dataSnapshot.getValue(Petshop.class);
                    email_petshop_perfil.setText(auth.getCurrentUser().getEmail());
                    nome_petshop.setText(petshop.getNome());
                    descricao_petshop.setText(petshop.getDescricaoPetshop());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    public void inicializaComponentes(){
        descricao_petshop = findViewById(R.id.descricao_petshop_perfil);
        nome_petshop = findViewById(R.id.nome_petshop_perfil);
        tipoCadastro = findViewById(R.id.tipoCadastro);
        logout = findViewById(R.id.sairLogout);
        tipoCadastroProduto = findViewById(R.id.tipoCadastroProduto);
        tipoCadastroServico = findViewById(R.id.tipoCadastroServico);
        email_petshop_perfil = findViewById(R.id.email_petshop_perfil);

        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        user = auth.getCurrentUser();
    }
}
