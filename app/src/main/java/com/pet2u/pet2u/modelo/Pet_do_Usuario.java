package com.pet2u.pet2u.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Pet_do_Usuario {
    private String idPet, nomePet, generoPet, racaPet, categoriaPet, nascimentoPet, castradoPet, emailUsuario, dataCadastro;
    private DatabaseReference databaseReference;

    public Pet_do_Usuario() {
    }

    public void salvar(String nomeNo, String nomeSubNo, String emailCriptografado){
        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child(nomeNo).child(emailCriptografado).child(nomeSubNo).child(this.idPet).setValue(this);
    }

    @Exclude
    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public String getNomePet() {
        return nomePet;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public String getGeneroPet() {
        return generoPet;
    }

    public void setGeneroPet(String generoPet) {
        this.generoPet = generoPet;
    }

    public String getRacaPet() {
        return racaPet;
    }

    public void setRacaPet(String racaPet) {
        this.racaPet = racaPet;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getCategoriaPet() {
        return categoriaPet;
    }

    public void setCategoriaPet(String categoriaPet) {
        this.categoriaPet = categoriaPet;
    }

    public String getNascimentoPet() {
        return nascimentoPet;
    }

    public void setNascimentoPet(String nascimentoPet) {
        this.nascimentoPet = nascimentoPet;
    }

    public String getCastradoPet() {
        return castradoPet;
    }

    public void setCastradoPet(String castradoPet) {
        this.castradoPet = castradoPet;
    }
}
