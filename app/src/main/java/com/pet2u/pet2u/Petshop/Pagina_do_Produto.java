package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.R;

public class Pagina_do_Produto extends AppCompatActivity {

    private Button botaoVoltar_paginaproduto, BotaoAdicionarAoCarrinho, BotaoFavoritarProduto;
    private TextView NomeProdutoPagina, DescricaoProdutoPagina, ValorProdutoPagina, ValorProdutoParcelado;
    private EditText CPF_digitado;

    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_do__produto);
        getSupportActionBar().hide();
        inicializaComponenetes();
        clicks();
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


        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
    }
}