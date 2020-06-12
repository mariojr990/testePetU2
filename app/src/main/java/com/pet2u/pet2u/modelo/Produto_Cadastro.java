package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Produto_Cadastro {
    private String nome, marca, emailPetShop, valor, idProduto, dataCadastro, categoria, descricao;
    private DatabaseReference databaseReference;

    public Produto_Cadastro() {
    }

    public void salvar(String nomeNo, String emailCriptografado,String nomeSubNo){
        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child(nomeNo).child(emailCriptografado).child(nomeSubNo).child(this.idProduto).setValue(this);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getEmailPetShop() {
        return emailPetShop;
    }

    public void setEmailPetShop(String emailPetShop) {
        this.emailPetShop = emailPetShop;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Exclude
    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

}
