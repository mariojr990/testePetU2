package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Produto {
    private String idProduto, nome, descricaoProduto, valor, imagem;
    private int height;
    private DatabaseReference databaseReference;

    public Produto() {
    }

    public void salvar(){

        databaseReference = Conexao.getFirebaseDatabase();
        //databaseReference.child("Produto").child(this.idProduto).setValue(this);
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoPetshop(String descricaoPetshop) {
        this.descricaoProduto = descricaoProduto;
    }

    @Exclude
    public String getidProduto() {
        return idProduto;
    }

    public void setidProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return nome;
    }
}
