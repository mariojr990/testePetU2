package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Servico_Cadastro {
    private String nomeServico, valorServico, descricaoServico, categoriaServico, emailPetShop, idServico, dataCadastroServico ;
    private DatabaseReference databaseReference;


    public Servico_Cadastro(){
    }

    public void salvar(String nomeNo,String emailPetShop, String nomeSubNo){
        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child(nomeNo).child(emailPetShop).child(nomeSubNo).child(this.idServico).setValue(this);
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

    public String getCategoriaServico() {
        return categoriaServico;
    }

    public void setCategoriaServico(String categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    public String getEmailPetShop() {
        return emailPetShop;
    }

    public void setEmailPetShop(String emailPetShop) {
        this.emailPetShop = emailPetShop;
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

}
