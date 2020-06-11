package com.pet2u.pet2u.modelo;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

public class Usuario {
    private String idUsuario, Nome, Email, Senha, CPF, Telefone, dataCadastro, tipoUsuario;
    private DatabaseReference databaseReference;

    public Usuario() {
    }


    public void salvar(){

        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child("Usuario").child(this.idUsuario).setValue(this);
    }

    public static String getIdUsuario_auth(){
        FirebaseAuth firebaseAuth = Conexao.getFirebaseAuth();
        return firebaseAuth.getCurrentUser().getUid();
    }
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = Conexao.getFirebaseAuth();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarTipoUsuario(String tipo){
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(tipo)
                    .build();
            user.updateProfile(profile);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    @Exclude
    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String Telefone) {
        this.Telefone = Telefone;
    }

    @Override
    public String toString() {
        return Nome;
    }
}
