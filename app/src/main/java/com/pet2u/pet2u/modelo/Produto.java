package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Produto {
    private String idProduto, nome, descricaoProduto, valor, categoria, tituloCategoria, imagem;
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

    public void setDescricaoProduto(String descricaoProduto) {
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTituloCategoria() {
        return tituloCategoria;
    }

    public void setTituloCategoria(String categoria) {
        this.tituloCategoria = tituloCategoria;
    }

    @Override
    public String toString() {
        return nome;
    }
}
