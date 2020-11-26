package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Pet_Adocao {
    private String idPetAdocao, nome, descricaoPetAdocao, idade;
    private DatabaseReference databaseReference;

    public Pet_Adocao(){
    }

    public void salvar(){
        databaseReference = Conexao.getFirebaseDatabase();
    }

    public String getIdPetAdocao() {
        return idPetAdocao;
    }

    public void setIdPetAdocao(String idPetAdocao) {
        this.idPetAdocao = idPetAdocao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricaoPetAdocao() {
        return descricaoPetAdocao;
    }

    public void setDescricaoPetAdocao(String descricaoPetAdocao) {
        this.descricaoPetAdocao = descricaoPetAdocao;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

//    public DatabaseReference getDatabaseReference() {
//        return databaseReference;
//    }
//
//    public void setDatabaseReference(DatabaseReference databaseReference) {
//        this.databaseReference = databaseReference;
//    }
    public String toString(){
        return nome;
    }
}
