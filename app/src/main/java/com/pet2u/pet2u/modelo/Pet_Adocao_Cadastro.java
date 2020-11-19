package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Pet_Adocao_Cadastro {
    private String nome, idade, emailPetShop, dataCadastro, idPetAdocao, descricao;
    private DatabaseReference databaseReference;

    public Pet_Adocao_Cadastro(){

    }

    public void salvar(String nomeNo, String emailCriptografado, String nomeSubNo){
        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child(nomeNo).child(emailCriptografado).child(nomeSubNo).child(idPetAdocao).setValue(this);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getEmailPetShop() {
        return emailPetShop;
    }

    public void setEmailPetShop(String emailPetShop) {
        this.emailPetShop = emailPetShop;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getIdPetAdocao() {
        return idPetAdocao;
    }

    public void setIdPetAdocao(String idPetAdocao) {
        this.idPetAdocao = idPetAdocao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

//    public DatabaseReference getDatabaseReference() {
//        return databaseReference;
//    }
//
//    public void setDatabaseReference(DatabaseReference databaseReference) {
//        this.databaseReference = databaseReference;
//    }
}
