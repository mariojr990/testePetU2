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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.DateCustom;
import com.pet2u.pet2u.Helper.Permissao;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Produto_Cadastro;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class CadastroProdutoActivity extends AppCompatActivity {

    private final static int CODIGO_SELECAO_FOTO = 1;
    public String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private Button botaoCadastrarProduto, botaoVoltar;
    private EditText campoNomeProduto, campoMarcaProduto, campoValorProduto;
    private ImageButton ButtonImageProduto;
    private Produto_Cadastro produto;
    private EditText descricaoProduto;
     private Spinner campoCategoria;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);
        Permissao.validarPermissoes(permissoesNecessarias, this,1);
        getSupportActionBar().hide();
        inicializaComponenetes();
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
                ButtonImageProduto.setImageURI(imageData);
                String nome = campoNomeProduto.getText().toString().trim();
                String nomefinal = nome.replace(" ", "");
                storageReference.child("FotoProduto/" + Criptografia.codificar(nomefinal)).putFile(imageData);
            }
        }
    }

    private void clicks(){

        ButtonImageProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CODIGO_SELECAO_FOTO);
            }
        });


        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PerfilPet_petshop.class));
                finish();
            }
        });

        botaoCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = campoNomeProduto.getText().toString().trim();
                String marca = campoMarcaProduto.getText().toString().trim();
                String valor = campoValorProduto.getText().toString().trim();
                valor = valor.replace(".", ",");
                String categoria = campoCategoria.getSelectedItem().toString();
                String descricaoProdutoo = descricaoProduto.getText().toString().trim();

                if (nome.isEmpty() || marca.isEmpty() || valor.isEmpty()){
                    alert("Preencha todos os campos");
                }
                else{
                    produto= new Produto_Cadastro();
                    String email = Criptografia.codificar(auth.getCurrentUser().getEmail());
                    produto.setIdProduto(UUID.randomUUID().toString());
                    produto.setEmailPetShop(email);
                    produto.setDataCadastro(DateCustom.dataAtual());
                    produto.setNome(nome);
                    produto.setCategoria(categoria);
                    produto.setDescricao(descricaoProdutoo);
                    produto.setMarca(marca);
                    produto.setValor(valor);
                    produto.salvar("Petshop", email, "produto");
                    exibirConfirmacao();
                    //alert("Uma novidade: Seu produto foi cadastrado com sucesso :P");
                    limparCampos();


                }
            }
        });
    }

    private void iniciarImagem(){
        String nome = campoNomeProduto.getText().toString().trim();
        String nomefinal = nome.replace(" ", "");

        storageReference.child("FotoProduto/" + Criptografia.codificar(nomefinal)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(ButtonImageProduto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("xesque", " A imagem não existe");
            }
        }) ;
    }

    private void alert(String msg){
        Toast.makeText(CadastroProdutoActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void exibirConfirmacao() {
        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(this);
        caixaDialogo.setTitle("Sucesso!");
        caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
        caixaDialogo.setMessage("Uma novidade: Seu produto foi cadastrado com sucesso :P");
        caixaDialogo.setPositiveButton("Voltar para Perfil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        caixaDialogo.setNegativeButton("Cadastrar outro produto", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(CadastroProdutoActivity.this, CadastroProdutoActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
        caixaDialogo.show();
    }

    private void limparCampos(){
        campoNomeProduto.setText("");
        campoValorProduto.setText(null);
        campoMarcaProduto.setText("");
        descricaoProduto.setText("");
    }

    private void inicializaComponenetes(){
        campoNomeProduto = findViewById(R.id.editTextNomeProduto);
        campoMarcaProduto = findViewById(R.id.editTextMarcaProduto);
        campoValorProduto = findViewById(R.id.editTextValorProduto);
        botaoVoltar = findViewById(R.id.botaoVoltarCadProduto);
        botaoCadastrarProduto = findViewById(R.id.botaoCadastrarProduto);
        campoCategoria = findViewById(R.id.ListaCategoria);
        descricaoProduto = findViewById(R.id.descricaoProduto);
        ButtonImageProduto = findViewById(R.id.ButtonImageProduto);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.categorias,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoCategoria.setAdapter(adapter);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
    }

}
