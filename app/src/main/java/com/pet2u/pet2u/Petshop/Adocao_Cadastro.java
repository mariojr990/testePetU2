package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.DateCustom;
import com.pet2u.pet2u.Helper.Permissao;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Pet_Adocao_Cadastro;

import java.util.UUID;

public class Adocao_Cadastro extends AppCompatActivity {

    private final static int CODIGO_SELECAO_FOTO = 1;
    public String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private EditText campoNomePetAdocao, campoIdadePetAdocao, campoDescricaoPetAdocao;
    private ImageButton fotoPetAdocao;
    private Button cadastrarPetAdocao, voltar;
    private Pet_Adocao_Cadastro pet;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adocao_cadastro);
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);
        getSupportActionBar().hide();
        inicializaComponenetes();
        clicks();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_SELECAO_FOTO){
            if (resultCode == RESULT_OK){
                Uri imageData = data.getData();
                fotoPetAdocao.setImageURI(imageData);
                String nome = campoNomePetAdocao.getText().toString().trim();
                storageReference.child("FotoPetAdocao/" +Criptografia.codificar(nome)).putFile(imageData);
            }
        }
    }

    private void clicks(){
        fotoPetAdocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CODIGO_SELECAO_FOTO);
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PerfilPet_petshop.class));
                finish();
            }
        });

        cadastrarPetAdocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = campoNomePetAdocao.getText().toString().trim();
                String idade = campoIdadePetAdocao.getText().toString().trim();
                String descricao = campoDescricaoPetAdocao.getText().toString().trim();


                if (nome.isEmpty() || idade.isEmpty() || descricao.isEmpty()){
                    alert("Preencha todos os Campos");
                }
                else{
                    pet = new  Pet_Adocao_Cadastro();
                    String email = Criptografia.codificar(auth.getCurrentUser().getEmail());
                    pet.setIdPetAdocao(UUID.randomUUID().toString());
                    pet.setEmailPetShop(email);
                    pet.setDataCadastro(DateCustom.dataAtual());
                    pet.setNome(nome);
                    pet.setIdade(idade);
                    pet.setDescricao(descricao);
                    pet.salvar("Petshop", email, "petAdocao");
                    exibirConfirmacao();
                    limparCampos();
                }
            }
        });
    }
    private void alert(String msg){
        Toast.makeText(Adocao_Cadastro.this, msg, Toast.LENGTH_SHORT).show();
    }
    private void exibirConfirmacao() {
        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(this);
        caixaDialogo.setTitle("Sucesso!");
        caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
        caixaDialogo.setMessage("Uma novidade: Seu pet foi cadastrado com sucesso :P");
        caixaDialogo.setPositiveButton("Voltar para Perfil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        caixaDialogo.setNegativeButton("Cadastrar outro pet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Adocao_Cadastro.this, Adocao_Cadastro.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
        caixaDialogo.show();
    }
    private void limparCampos(){
        campoNomePetAdocao.setText("");
        campoIdadePetAdocao.setText(null);
        campoDescricaoPetAdocao.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultados : grantResults) {
            if (permissaoResultados == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negados");
        builder.setMessage("Para utilizar o app é necessario aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void inicializaComponenetes() {
        campoNomePetAdocao = findViewById(R.id.editTextNomePetAdocao);
        campoIdadePetAdocao = findViewById(R.id.editTextIdadePetAdocao);
        campoDescricaoPetAdocao = findViewById(R.id.descricaoPetAdocao);
        fotoPetAdocao = findViewById(R.id.buttonImagePetAdocao);
        voltar = findViewById(R.id.botaoVoltarCadPetAdocao);
        cadastrarPetAdocao = findViewById(R.id.botaoCadAdocao);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
    }


}