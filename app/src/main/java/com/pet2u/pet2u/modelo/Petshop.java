package com.pet2u.pet2u.modelo;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.pet2u.pet2u.ConexaoDB.Conexao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Petshop {
    private String idPetshop, nome, razao_social, cnpj, telefone, cep, uf, bairro, cidade, endereco, numero, complemento, senha, email, dataCadastro, tipoUsuario, descricaoPetshop, score;
    private DatabaseReference databaseReference;

    public Petshop() {
    }

    public void salvar(){

        databaseReference = Conexao.getFirebaseDatabase();
        databaseReference.child("Petshop").child(this.idPetshop).setValue(this);
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDescricaoPetshop() {
        return descricaoPetshop;
    }

    public void setDescricaoPetshop(String descricaoPetshop) {
        this.descricaoPetshop = descricaoPetshop;
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
    public String getidPetshop() {
        return idPetshop;
    }

    public void setidPetshop(String idPetshop) {
        this.idPetshop = idPetshop;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazao_social() {
        return razao_social;
    }

    public void setRazao_social(String razao_social) {
        this.razao_social = razao_social;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome;
    }

    public static Comparator<Petshop> ByAlfabetica = new Comparator<Petshop>() {

        public int compare(Petshop n1, Petshop n2)
        {
            return String.valueOf(n1.nome).compareTo(String.valueOf(n2.nome));
        }
    };

    public static Comparator<Petshop> ByData = new Comparator<Petshop>() {
        @Override
        public int compare(Petshop three, Petshop four) {
            SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");

            Log.d("" , three.dataCadastro);

            String[] splitStr1 = three.dataCadastro.split("\\s+");
            String[] splitStr2 = four.dataCadastro.split("\\s+");
            System.out.println(splitStr1[0]);

            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdformat.parse(splitStr1[0]);
                d2 = sdformat.parse(splitStr2[0]);
            }
            catch(Exception ex) {

            }

            return d1.compareTo(d2);
        }
    };

    public static Comparator<Petshop> ByScore = new Comparator<Petshop>() {
        @Override
        public int compare(Petshop five, Petshop six) {
            return - Double.valueOf(five.score).compareTo(Double.valueOf(six.score));
        }
    };

//    @Override
//    public int compareTo(Petshop other) {
//        return this.nome.compareTo(other.nome);
//    }
}