package com.pet2u.pet2u.Usuario;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.Permissao;
import com.pet2u.pet2u.Login.MainActivity;
import com.pet2u.pet2u.Petshop.ListagemPetshop_Activity;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Sobre.Sobre;
import com.pet2u.pet2u.modelo.Usuario;
import com.squareup.picasso.Picasso;

public class PerfilUsuario_Activity extends AppCompatActivity {
    private TextView nomeCompleto, telefone, email;
    private Button botao_alterarSenha, botao_alterarTelefone, botao_alterarNome, botao_petshops_list;
    private ImageButton fotoPerfilUsuario;
    private final static int CODIGO_SELECAO_FOTO = 2;
    public String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        Permissao.validarPermissoes(permissoesNecessarias, this,1);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClick();
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

    private void eventoClick() {

        botao_alterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EsqueceuSenha_Activity.class));
            }
        });

        botao_alterarTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarTelefone_aActivity.class));
            }
        });

        botao_alterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarNomeActivity.class));
            }
        });

        botao_petshops_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListagemPetshop_Activity.class));
            }
        });
        fotoPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CODIGO_SELECAO_FOTO);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODIGO_SELECAO_FOTO ) {
            if (resultCode == RESULT_OK) {
                Uri imageData=data.getData();
                fotoPerfilUsuario.setImageURI(imageData);
                storageReference.child("FotoPerfilUsuario/" + Criptografia.codificar(user.getEmail())).putFile(imageData);
            }
        }
    }

    public void ClickListagemPetshop(View view){
        startActivity(new Intent(getApplicationContext(), ListagemPetshop_Activity.class));
    }
    public void clickLogoutUsuario(View view){
        Conexao.logOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void clickSobre(View view){
        startActivity(new Intent(getApplicationContext(), Sobre.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        storageReference = Conexao.getFirebaseStorage();
        user = auth.getCurrentUser();
        verificaUser();
    }

    private void verificaUser() {
        if(user == null){
            finish();
        }else{
            String idUsuarioFoto = Criptografia.codificar(user.getEmail());
            storageReference.child("FotoPerfilUsuario/" + idUsuarioFoto).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerInside().into(fotoPerfilUsuario);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("xesque", " A imagem não existe");
                }
            }) ;

            //CRIA A LIGAÇÃO ENTRE O USUÁRIO LOGADO E O DATABASE DELE
            String emailUsuario = user.getEmail();
            String idUsuario = Criptografia.codificar(emailUsuario);
            DatabaseReference usuarioRef = databaseReference.child("Usuario").child(idUsuario);

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    nomeCompleto.setText(usuario.getNome());
                    email.setText(usuario.getEmail());
                    telefone.setText(usuario.getTelefone());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void inicializaComponentes(){
        nomeCompleto = findViewById(R.id.nomeCompletoUsuario);
        telefone = findViewById(R.id.editarTelefone);
        email = findViewById(R.id.editarEmail);
        botao_alterarSenha = findViewById(R.id.botaoAlterarSenha);
        botao_alterarTelefone = findViewById(R.id.botaoAlterarTelefone);
        botao_alterarNome= findViewById(R.id.botaoAlterarNome);
        botao_petshops_list = findViewById(R.id.botaoIrpara_listagemPetshop_perfilUsu);
        fotoPerfilUsuario = findViewById(R.id.botaoSelecionarFotoPerfil);

    }

}
