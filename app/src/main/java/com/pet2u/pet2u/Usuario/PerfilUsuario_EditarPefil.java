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
import com.pet2u.pet2u.Petshop.PerfilPet_Usuario;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Sobre.Sobre;
import com.pet2u.pet2u.modelo.Usuario;
import com.squareup.picasso.Picasso;

public class PerfilUsuario_EditarPefil extends AppCompatActivity {

    private final static int CODIGO_SELECAO_FOTO = 1;
    public String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private TextView nomeCompleto, telefone;
    private Button botao_alterarSenha, botao_alterarTelefone, botao_alterarNome, botao_petshops_list, voltar_editarpefilusuario;
    private ImageButton fotoPerfilUsuario;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario__editar_pefil);
        Permissao.validarPermissoes(permissoesNecessarias, this,1);
        getSupportActionBar().hide();
        inicializaComponentes();
        verificaUser();
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

    @Override
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

        voltar_editarpefilusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void verificaUser() {
        if(user == null){
            finish();
        }else{
            //CRIA A LIGAÇÃO ENTRE O USUÁRIO LOGADO E O DATABASE DELE
            String emailUsuario = user.getEmail();
            String idUsuario = Criptografia.codificar(emailUsuario);
            DatabaseReference usuarioRef = databaseReference.child("Usuario").child(idUsuario);

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
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

                    nomeCompleto.setText(usuario.getNome());
                    telefone.setText(usuario.getTelefone());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    private void inicializaComponentes(){
        nomeCompleto = findViewById(R.id.nomeCompletoUsuario2);
        telefone = findViewById(R.id.editarTelefone2);
        botao_alterarSenha = findViewById(R.id.botaoAlterarSenha2);
        botao_alterarTelefone = findViewById(R.id.botaoAlterarTelefone2);
        botao_alterarNome= findViewById(R.id.botaoAlterarNome2);
        botao_petshops_list = findViewById(R.id.botaoIrpara_listagemPetshop_perfilUsu2);
        fotoPerfilUsuario = findViewById(R.id.botaoSelecionarFotoPerfil2);
        voltar_editarpefilusuario = findViewById(R.id.voltar_editarpefilusuario);

        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        storageReference = Conexao.getFirebaseStorage();
        user = auth.getCurrentUser();

    }
}