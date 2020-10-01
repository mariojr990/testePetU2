package com.pet2u.pet2u.Usuario.CadastroMeuPet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.DateCustom;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;
import com.pet2u.pet2u.modelo.Pet_do_Usuario;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Cadastro_Meu_Pet_7 extends AppCompatActivity {

    private TextView petnameText;
    private ImageView imagePet;
    private TextView petname;
    private Button buttonConcluir;
    private Pet_do_Usuario petUsuario;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__meu__pet_7);
        getSupportActionBar().hide();

        inicializarComponentes();
        clicks();
        inicializarFotoDePerfil();
    }
    private void clicks(){
        buttonConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePet = getIntent().getExtras().getString("nomePet");
                String generoPet = getIntent().getExtras().getString("GeneroPet");
                String racaPet = getIntent().getExtras().getString("racaPet");
                String categoriaPet = getIntent().getExtras().getString("nomeCategoriaPet");
                String email = Criptografia.codificar(auth.getCurrentUser().getEmail());

                petUsuario= new Pet_do_Usuario();
                petUsuario.setIdPet(UUID.randomUUID().toString());
                petUsuario.setEmailUsuario(email);
                petUsuario.setDataCadastro(DateCustom.dataAtual());
                petUsuario.setNomePet(nomePet);
                petUsuario.setGeneroPet(generoPet);
                petUsuario.setRacaPet(racaPet);
                petUsuario.setCategoriaPet(categoriaPet);

                if(categoriaPet.equals("Peixe")){
                    petUsuario.setNascimentoPet("Não tem");
                    petUsuario.setCastradoPet("Não tem");

                }else{
                    String nascimentoPet = getIntent().getExtras().getString("anoNascimentoPet");
                    String castradoPet = getIntent().getExtras().getString("castradoPet");
                    petUsuario.setNascimentoPet(nascimentoPet);
                    petUsuario.setCastradoPet(castradoPet);
                    petUsuario.salvar("Usuario","petUsuario", email);
                }

                petUsuario.salvar("Usuario","petUsuario", email);

                Intent intent = new Intent(getApplicationContext(), PerfilUsuario_Activity.class);
                startActivity(intent);
                finish();


            }
        });

    }

    private void inicializarFotoDePerfil() {
        String nome = auth.getCurrentUser().getEmail();
        nome += getIntent().getExtras().getString("nomePet");
        String nomefinal = nome.replace(" ", "");
        storageReference.child("FotoPets/" + Criptografia.codificar(nomefinal)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(imagePet);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("xesque", " A imagem não existe");
            }
        }) ;
    }




    private void inicializarComponentes() {
        petnameText = findViewById(R.id.petnameText);
        petname = findViewById(R.id.petname);
        imagePet = findViewById(R.id.imagePet);
        buttonConcluir = findViewById(R.id.buttonConcluir);

        String generoPet = getIntent().getExtras().getString("GeneroPet");
        if(generoPet.equals("Macho")){
            petnameText.setText("Oi, " + getIntent().getExtras().getString("nomePet") + " cadastrado com sucesso!!!");
        }else{
            petnameText.setText("Oi, " + getIntent().getExtras().getString("nomePet") + " cadastrada com sucesso!!!");
        }

        petname.setText(getIntent().getExtras().getString("nomePet"));

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
    }
}