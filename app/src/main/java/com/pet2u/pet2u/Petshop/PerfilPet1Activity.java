package com.pet2u.pet2u.Petshop;

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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;

public class PerfilPet1Activity extends AppCompatActivity {
    private static final int CODIGO_SELECAO_FOTO = 1;
    public String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private TextView descricao_petshop, nome_petshop, email_petshop_perfil;
    private ImageButton botaoSelecionarFoto;
    private ImageView fotoPerfilPetshop;
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference storageReference;
    private Uri fotoPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet1);

        //validar Permissoes
        Permissao.validarPermissoes(permissoesNecessarias, this,1);

        getSupportActionBar().hide();
        inicializaComponentes();
        verificaUser();

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

    public void ClickNovoProduto(View view ){
        startActivity(new Intent(getApplicationContext(), CadastroProdutoActivity.class));
    }
    public void clickNovoServico(View view ){
        startActivity(new Intent(getApplicationContext(), CadastroServicoActivity.class));
    }
    public void clickLogout(View view ){
        Conexao.logOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODIGO_SELECAO_FOTO ) {
            if (resultCode == RESULT_OK) {
                Uri imageData=data.getData();
                botaoSelecionarFoto.setImageURI(imageData);
                //storageReference.child("FotoPerfil.jpg");
                storageReference.child("FotoPerfil/" + Criptografia.codificar(user.getEmail()) + "/FotoPerfil.jpg").putFile(imageData);
            }
        }
    }

    private void verificaUser() {
        if(user == null){
            finish();
        }else{

            //CRIA A LIGAÇÃO ENTRE O USUÁRIO LOGADO E O DATABASE DELE
            String idUsuario = Criptografia.codificar(user.getEmail());
            DatabaseReference usuarioRef = databaseReference.child("Petshop").child(idUsuario);

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Petshop petshop = dataSnapshot.getValue(Petshop.class);
                    email_petshop_perfil.setText(auth.getCurrentUser().getEmail());
                    nome_petshop.setText(petshop.getNome());
                    descricao_petshop.setText(petshop.getDescricaoPetshop());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    public void inicializaComponentes(){
        descricao_petshop = findViewById(R.id.descricao_petshop_perfil);
        nome_petshop = findViewById(R.id.nome_petshop_perfil);
        email_petshop_perfil = findViewById(R.id.email_petshop_perfil);
        botaoSelecionarFoto = findViewById(R.id.BotaoSelecionarFoto);
        fotoPerfilPetshop = findViewById(R.id.BotaoSelecionarFoto);

        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        user = auth.getCurrentUser();
        storageReference = Conexao.getFirebaseStorage();
    }
}
