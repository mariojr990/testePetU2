package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Servico_Cadastro {
    private String nomeServico, valorServico, descricaoServico, emailPetShopServico, idServico, dataCadastroServico ;
    private DatabaseReference databaseReference;

    public Servico_Cadastro(){

    }
    public void salvar(String nomeNo,String emailPetShopServico, String nomeSubNo){
        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child(nomeNo).child(emailPetShopServico).child(nomeNo).child(this.idServico).setValue(this);
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public String getValorServico() {
        return valorServico;
    }

    public void setValorServico(String valorServico) {
        this.valorServico = valorServico;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public String getEmailPetShopServico() {
        return emailPetShopServico;
    }

    public void setEmailPetShopServico(String emailPetShopServico) {
        this.emailPetShopServico = emailPetShopServico;
    }

    @Exclude
    public String getIdServico() {
        return idServico;
    }

    public void setIdServico(String idServico) {
        this.idServico = idServico;
    }

    public String getDataCadastroServico() {
        return dataCadastroServico;
    }

    public void setDataCadastroServico(String dataCadastroServico) {
        this.dataCadastroServico = dataCadastroServico;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
}
