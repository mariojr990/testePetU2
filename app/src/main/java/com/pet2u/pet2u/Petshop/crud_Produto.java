package com.pet2u.pet2u.Petshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.pet2u.pet2u.R;
import com.squareup.picasso.Picasso;

public class crud_Produto extends AppCompatActivity {

    private Button botaoVoltar_paginaproduto;
    private TextView NomeProdutoPagina, DescricaoProdutoPagina, ValorProdutoPagina;
    private ImageView fotoPerfilProduto;

    private StorageReference storageReference;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String title;
    private String nome;
    private int posicao;
    private Dialog dialog, dialogDeletarProduto;
    private EditText dialogNovoUpdate;
    private String novoUpdate, novoNome, novaDescricao, novoValor;
    private boolean updatedNome = false, updatedDescricao = false, updatedValor = false;
    private boolean nomeDialogClicked = false, descricaoDialogClicked = false, valorDialogClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud__produto);
        getSupportActionBar().hide();
        inicializaComponenetes();

        title = getIntent().getExtras().getString("nomeProduto");
        nome = Criptografia.codificar(title.replace(" ", ""));

        storageReference.child("FotoProduto/" + nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(fotoPerfilProduto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("erroImagem", " A imagem não existe");
            }
        });

        NomeProdutoPagina.setText(getIntent().getExtras().getString("nomeProduto"));
        DescricaoProdutoPagina.setText(getIntent().getExtras().getString("descricaoProduto"));
        ValorProdutoPagina.setText("R$ " + getIntent().getExtras().getString("valorProduto"));

    }

    @Override
    public void onBackPressed() {
        BotaoVoltarListaProdutosPetshop(botaoVoltar_paginaproduto);
    }


    public void BotaoVoltarListaProdutosPetshop(View view) {
        Intent returnIntent = new Intent();
        if (updatedNome) {
            returnIntent.putExtra("updateNome", updatedNome);
            returnIntent.putExtra("novoNome", novoNome);
        }
        if (updatedDescricao) {
            returnIntent.putExtra("updateDescricao", updatedDescricao);
            returnIntent.putExtra("novaDescricao", novaDescricao);
        }
        if (updatedValor) {
            returnIntent.putExtra("updateValor", updatedValor);
            returnIntent.putExtra("novoValor", novoValor);
        }
        returnIntent.putExtra("position", posicao);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void SimExcluirProduto(View view) {
        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(Criptografia.codificar(user.getEmail())).child("produto");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int indexProduto = 0;
                for (DataSnapshot produto : dataSnapshot.getChildren()){
                    Log.d("xesque", posicao + " POSICAO");
                    if (posicao == indexProduto) {
                        produto.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(crud_Produto.this, "Produto Removido!", Toast.LENGTH_SHORT);
                            }
                        });
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("delete", true);
                        returnIntent.putExtra("position", posicao);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    indexProduto++;
                }
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }

    public void NaoExcluirProduto(View view) {
        dialogDeletarProduto.dismiss();
    }

    public void ExlcuirProduto(View view) {
        dialogDeletarProduto.setContentView(R.layout.dialog_confirmacao_excluir_produto);
        dialogDeletarProduto.show();
    }

    public void EditarNomeProduto(View view) {
        dialog.setContentView(R.layout.dialog_atualizar_produto_petshop);
        TextView dialogTitle = dialog.findViewById(R.id.dialogNomeProdutoAtualizado);
        dialogNovoUpdate = dialog.findViewById(R.id.dialogNovoUpdate);

        dialogTitle.setText(getText(R.string.nome_produto_atual) + " " + getIntent().getExtras().getString("nomeProduto"));

        dialogNovoUpdate.setHint(getText(R.string.novo_nome_produto));

        dialog.show();

        nomeDialogClicked = true;
        descricaoDialogClicked = false;
        valorDialogClicked = false;
    }

    public void EditarDescricaoProduto(View view) {
        dialog.setContentView(R.layout.dialog_atualizar_produto_petshop);
        TextView dialogTitle = dialog.findViewById(R.id.dialogNomeProdutoAtualizado);
        dialogNovoUpdate = dialog.findViewById(R.id.dialogNovoUpdate);

        dialogTitle.setText(getText(R.string.descricao_produto_atual) + " " + getIntent().getExtras().getString("descricaoProduto"));

        dialogNovoUpdate.setHint(getText(R.string.nova_descricao_produto));

        dialog.show();

        nomeDialogClicked = false;
        descricaoDialogClicked = true;
        valorDialogClicked = false;
    }
    public void EditarPrecoProduto(View view) {
        dialog.setContentView(R.layout.dialog_atualizar_produto_petshop);

        TextView dialogTitle = dialog.findViewById(R.id.dialogNomeProdutoAtualizado);
        dialogNovoUpdate = dialog.findViewById(R.id.dialogNovoUpdate);

        dialogNovoUpdate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        dialogTitle.setText(getText(R.string.preco_produto_atual) + " " + getIntent().getExtras().getString("valorProduto"));

        dialogNovoUpdate.setHint(getText(R.string.novo_preco_produto));

        dialog.show();

        nomeDialogClicked = false;
        descricaoDialogClicked = false;
        valorDialogClicked = true;
    }

    public void BotaoAtualizarProduto(View view) {
        if (nomeDialogClicked) {
            AtualizarValorDatabase("nome");
        }
        else if (descricaoDialogClicked) {
            AtualizarValorDatabase("descricao");
        }
        else if (valorDialogClicked) {
            AtualizarValorDatabase("valor");
        }
    }

    public void AtualizarValorDatabase(final String valor) {
        if (valor.equals("descricao")) {
            if (dialogNovoUpdate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, preencha com uma nova descrição!", Toast.LENGTH_SHORT);
                return;
            }
        }
        else if (valor.equals("nome")) {
            if (dialogNovoUpdate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, preencha com um novo nome!", Toast.LENGTH_SHORT);
                return;
            }
        }
        else if (valor.equals("valor")) {
            if (dialogNovoUpdate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, preencha com um novo preço!", Toast.LENGTH_SHORT);
                return;
            }
        }

        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(Criptografia.codificar(user.getEmail())).child("produto");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int indexProduto = 0;
                for (DataSnapshot produto : dataSnapshot.getChildren()){
                    if (posicao == indexProduto) {
                        novoUpdate = dialogNovoUpdate.getText().toString();
                        produto.child(valor).getRef().setValue(novoUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(crud_Produto.this, "Produto Atualizado!", Toast.LENGTH_SHORT);
                            }
                        });

                        if (valor.equals("descricao")) {
                            updatedDescricao = true;
                            novaDescricao = novoUpdate;
                            DescricaoProdutoPagina.setText(novaDescricao);
                        }
                        else if (valor.equals("nome")) {
                            updatedNome = true;
                            novoNome = novoUpdate;
                            NomeProdutoPagina.setText(novoNome);
                        }
                        else if (valor.equals("valor")) {
                            updatedValor = true;
                            novoValor = novoUpdate;
                            ValorProdutoPagina.setText("R$ " + novoValor);
                        }
                        dialog.dismiss();
                    }
                    indexProduto++;
                }
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }

    public void BotaoFecharDialogo(View view) {
        dialog.dismiss();
    }

    private void inicializaComponenetes(){
        botaoVoltar_paginaproduto = findViewById(R.id.botaoVoltar_paginaproduto2);
        posicao = getIntent().getExtras().getInt("posicaoProduto");

        NomeProdutoPagina = findViewById(R.id.NomeProdutoPagina2);
        DescricaoProdutoPagina = findViewById(R.id.DescricaoProdutoPagina2);
        ValorProdutoPagina = findViewById(R.id.ValorProdutoPagina2);

        fotoPerfilProduto = findViewById(R.id.ImagemProdutoPagina2);

        //Componentes Dialog
        dialog = new Dialog(this);
        dialogDeletarProduto = new Dialog(this);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        databaseReference = Conexao.getFirebaseDatabase();

    }
}