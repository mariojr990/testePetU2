package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Produto_Cadastro {
    private String nome;
    private String marca;
    private String emailPetShop;
    private String valor;
    private String idProduto;
    private String dataCadastro;
    private DatabaseReference databaseReference;

    public Produto_Cadastro() {
    }

    public void salvar(){
        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child("Produto").child(this.idProduto).setValue(this);
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
