package com.pet2u.pet2u.Petshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.AdapterListaProdutosPet;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Produto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class crud_Produto extends AppCompatActivity {

    private Button botaoVoltar_paginaproduto, BotaoEditarNome, BotaoEditarNomeProd, BotaoEditarPrecoProd, BotaoEditarDescProd, BotaoEditarImagemProd, BotaoExcluirProduto;
    private TextView NomeProdutoPagina, DescricaoProdutoPagina, ValorProdutoPagina;
    private ImageView fotoPerfilProduto;

    private StorageReference storageReference;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String title;
    private String nome;
    private int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud__produto);
        getSupportActionBar().hide();
        inicializaComponenetes();
        clicks();

        title = getIntent().getExtras().getString("nomeProduto");
        nome = Criptografia.codificar(title.replace(" ", ""));

        storageReference.child("FotoProduto/" + nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(fotoPerfilProduto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("erroImagem", " A imagem não existe");
            }
        });

        NomeProdutoPagina.setText(getIntent().getExtras().getString("nomeProduto"));
        DescricaoProdutoPagina.setText(getIntent().getExtras().getString("descricaoProduto"));
        ValorProdutoPagina.setText("R$ " + getIntent().getExtras().getString("valorProduto"));

    }

    private void clicks() {
        botaoVoltar_paginaproduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void inicializaComponenetes(){
        botaoVoltar_paginaproduto = findViewById(R.id.botaoVoltar_paginaproduto2);
        BotaoEditarNome = findViewById(R.id.BotaoEditarNome);
        BotaoEditarDescProd = findViewById(R.id.botaoEditarDescricao);
        BotaoEditarImagemProd = findViewById(R.id.botaoEditarImagem);
        BotaoEditarPrecoProd = findViewById(R.id.botaoEditarPreco);
        BotaoExcluirProduto = findViewById(R.id.BotaoExcluirProduto);
        posicao = getIntent().getExtras().getInt("posicaoProduto");

        NomeProdutoPagina = findViewById(R.id.NomeProdutoPagina2);
        DescricaoProdutoPagina = findViewById(R.id.DescricaoProdutoPagina2);
        ValorProdutoPagina = findViewById(R.id.ValorProdutoPagina2);

        fotoPerfilProduto = findViewById(R.id.ImagemProdutoPagina2);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        databaseReference = Conexao.getFirebaseDatabase();

    }

    public void ExlcuirProduto(View view) {
        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(Criptografia.codificar(user.getEmail())).child("produto");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int indexProduto = 0;
                for (DataSnapshot produto : dataSnapshot.getChildren()){
                    Log.d("xesque", posicao + " POSICAO");
                    if (posicao == indexProduto) {
                        produto.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(crud_Produto.this, "Produto Removido!", Toast.LENGTH_SHORT);
                            }
                        });
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("delete", true);
                        returnIntent.putExtra("position", posicao);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    indexProduto++;
                }
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }
}