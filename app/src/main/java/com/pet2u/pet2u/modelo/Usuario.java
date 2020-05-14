package com.pet2u.pet2u.modelo;


public class Usuario {
    private String campoNome, campoEmail, campoSenha, campoCPF, campoTelefone;

    public Usuario() {
    }

    public String getCampoNome() {
        return campoNome;
    }

    public void setCampoNome(String campoNome) {
        this.campoNome = campoNome;
    }

    public String getCampoEmail() {
        return campoEmail;
    }

    public void setCampoEmail(String campoEmail) {
        this.campoEmail = campoEmail;
    }

    public String getCampoSenha() {
        return campoSenha;
    }

    public void setCampoSenha(String campoSenha) {
        this.campoSenha = campoSenha;
    }

    public String getCampoCPF() {
        return campoCPF;
    }

    public void setCampoCPF(String campoCPF) {
        this.campoCPF = campoCPF;
    }

    public String getCampoTelefone() {
        return campoTelefone;
    }

    public void setCampoTelefone(String campoTelefone) {
        this.campoTelefone = campoTelefone;
    }

    @Override
    public String toString() {
        return campoNome;
    }
}
