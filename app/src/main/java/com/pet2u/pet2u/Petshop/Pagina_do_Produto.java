package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.squareup.picasso.Picasso;

public class Pagina_do_Produto extends AppCompatActivity {

    private Button botaoVoltar_paginaproduto, BotaoAdicionarAoCarrinho, BotaoFavoritarProduto;
    private TextView NomeProdutoPagina, DescricaoProdutoPagina, ValorProdutoPagina, ValorProdutoParcelado;
    private EditText CPF_digitado;
    private ImageView fotoPerfilProduto;

    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_do__produto);
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
        String valorProdutoparcelado = getIntent().getExtras().getString("valorProduto");
        String g = valorProdutoparcelado.replace(",", ".");
        float valorparceladototal = Float.parseFloat(g);
        float a = valorparceladototal/2.0f;
        a = (float) (Math.round(a*100.0)/100.0);
        String b = "ou 2x R$ "+ a + " s/ juros";
        ValorProdutoParcelado.setText(b);
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
        botaoVoltar_paginaproduto = findViewById(R.id.botaoVoltar_paginaproduto);
        BotaoAdicionarAoCarrinho = findViewById(R.id.BotaoAdicionarAoCarrinho);
        BotaoFavoritarProduto = findViewById(R.id.BotaoFavoritarProduto);

        NomeProdutoPagina = findViewById(R.id.NomeProdutoPagina);
        DescricaoProdutoPagina = findViewById(R.id.DescricaoProdutoPagina);
        ValorProdutoPagina = findViewById(R.id.ValorProdutoPagina);
        ValorProdutoParcelado = findViewById(R.id.ValorProdutoParcelado);

        CPF_digitado = findViewById(R.id.CPF_digitado);

        fotoPerfilProduto = findViewById(R.id.ImagemProdutoPagina);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
    }
}