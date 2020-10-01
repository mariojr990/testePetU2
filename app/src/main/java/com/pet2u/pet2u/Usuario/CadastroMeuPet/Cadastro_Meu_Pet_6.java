package com.pet2u.pet2u.Usuario.CadastroMeuPet;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.Permissao;
import com.pet2u.pet2u.R;

public class Cadastro_Meu_Pet_6 extends AppCompatActivity {

    private final static int CODIGO_SELECAO_FOTO = 1;

    public String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private TextView botaoAdicionarDepois_CadMeuPet6, txtMensagemPrincipal6;
    private ImageView imagemCategoriaPetCadastrado6;
    private ImageButton buttonImagemDoBichinho;
    private Button buttonAdicionarFoto;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_6);
        Permissao.validarPermissoes(permissoesNecessarias, this,1);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for( int permissaoResultados : grantResults ){
            if(permissaoResultados == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
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
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODIGO_SELECAO_FOTO ) {
            if (resultCode == RESULT_OK) {
                Uri imageData=data.getData();
                buttonImagemDoBichinho.setImageURI(imageData);
                String nome = auth.getCurrentUser().getEmail();
                nome += getIntent().getExtras().getString("nomePet");
                String nomefinal = nome.replace(" ", "");
                storageReference.child("FotoPets/" + Criptografia.codificar(nomefinal)).putFile(imageData);
            }
        }
    }

    private void clicks() {

        buttonImagemDoBichinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CODIGO_SELECAO_FOTO);
            }
        });

        buttonAdicionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarIntent();
            }
        });

        botaoAdicionarDepois_CadMeuPet6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarIntent();
            }
        });


    }

    private void inicializarComponentes() {
        botaoAdicionarDepois_CadMeuPet6 = findViewById(R.id.botaoAdicionarDepois_CadMeuPet6);
        imagemCategoriaPetCadastrado6 = findViewById(R.id.imagemCategoriaPetCadastrado6);
        buttonImagemDoBichinho = findViewById(R.id.buttonImagemDoBichinho);
        txtMensagemPrincipal6 = findViewById(R.id.txtMensagemPrincipal6);
        buttonAdicionarFoto = findViewById(R.id.buttonAdicionarFoto);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();

        txtMensagemPrincipal6.setText(getIntent().getExtras().getString("nomeUsuario")+ ", adicione uma foto do seu bichinho e deixe o perfil do " + getIntent().getExtras().getString("nomePet") + " ainda mais completo!");

        String nomeCategoriaPet = getIntent().getExtras().getString("nomeCategoriaPet");
        verificarImagem(nomeCategoriaPet);
    }

    private void iniciarIntent(){
        String categoria = getIntent().getExtras().getString("nomeCategoriaPet");
        if(categoria.equals("Peixe")){
            Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_7.class);
            intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
            intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
            intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
            intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
            intent.putExtra("racaPet", getIntent().getExtras().getString("racaPet"));
            startActivity(intent);
        }else{

            Intent intent = new Intent(getApplicationContext(), Cadastro_Meu_Pet_7.class);
            intent.putExtra("nomeUsuario", getIntent().getExtras().getString("nomeUsuario"));
            intent.putExtra("nomeCategoriaPet", getIntent().getExtras().getString("nomeCategoriaPet"));
            intent.putExtra("nomePet", getIntent().getExtras().getString("nomePet"));
            intent.putExtra("GeneroPet", getIntent().getExtras().getString("GeneroPet"));
            intent.putExtra("racaPet", getIntent().getExtras().getString("racaPet"));
            intent.putExtra("anoNascimentoPet",  getIntent().getExtras().getString("anoNascimentoPet"));
            intent.putExtra("castradoPet", getIntent().getExtras().getString("castradoPet"));
            startActivity(intent);
        }
    }

    private void verificarImagem(String categoria){
        switch (categoria){
            case "Cachorro":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.dog_icon);
                break;

            case "Gato":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.cat_icon);
                break;

            case "Pássaro":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.bird_icon);
                break;

            case "Roedor":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.rodent_icon);
                break;

            case "Réptil":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.reptil_icon);
                break;

            case "Peixe":
                imagemCategoriaPetCadastrado6.setImageResource(R.drawable.fish_icon);
                break;
        }
    }
}