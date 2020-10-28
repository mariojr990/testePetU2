package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Servico {
    private String nomeServico, descricaoServico, tituloCategoria, valorServico;
    private DatabaseReference databaseReference;

    public Servico(){
    }

    public void salvar(){
        databaseReference = Conexao.getFirebaseDatabase();
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public String getTituloCategoria() {
        return tituloCategoria;
    }

    public void setTituloCategoria(String tituloCategoria) {
        this.tituloCategoria = tituloCategoria;
    }

    public String getValorServico() {
        return valorServico;
    }

    public void setValorServico(String valorServico) {
        this.valorServico = valorServico;
    }



//    public DatabaseReference getDatabaseReference() {
//        return databaseReference;
//    }
//
//    public void setDatabaseReference(DatabaseReference databaseReference) {
//        this.databaseReference = databaseReference;
//    }
}
