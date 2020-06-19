package com.pet2u.pet2u.Petshop;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.squareup.picasso.Picasso;

public class crud_Produto extends AppCompatActivity {

    private Button botaoVoltar_paginaproduto, BotaoEditarProduto, BotaoExcluirProduto;
    private TextView NomeProdutoPagina, DescricaoProdutoPagina, ValorProdutoPagina;
    private ImageView fotoPerfilProduto;

    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud__produto);
        getSupportActionBar().hide();
        inicializaComponenetes();
        clicks();

        String title = getIntent().getExtras().getString("nomeProduto");
        String nome = Criptografia.codificar(title.replace(" ", ""));

        storageReference.child("FotoProduto/" + nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(fotoPerfilProduto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("xesque", " A imagem não existe");
            }
        }) ;

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
        BotaoEditarProduto = findViewById(R.id.BotaoEditarProduto);
        BotaoExcluirProduto = findViewById(R.id.BotaoExcluirProduto);

        NomeProdutoPagina = findViewById(R.id.NomeProdutoPagina2);
        DescricaoProdutoPagina = findViewById(R.id.DescricaoProdutoPagina2);
        ValorProdutoPagina = findViewById(R.id.ValorProdutoPagina2);

        fotoPerfilProduto = findViewById(R.id.ImagemProdutoPagina2);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
    }
}