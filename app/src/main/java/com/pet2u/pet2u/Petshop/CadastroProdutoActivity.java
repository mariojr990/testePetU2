package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.DateCustom;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Produto_Cadastro;
import java.util.UUID;

public class CadastroProdutoActivity extends AppCompatActivity {
    private Button botaoCadastrarProduto, botaoVoltar;
    private EditText campoNomeProduto, campoMarcaProduto, campoValorProduto;
    private Produto_Cadastro produto;
    private EditText descricaoProduto;
    private FirebaseAuth auth;
    private Spinner campoCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);
        getSupportActionBar().hide();
        inicializaComponenetes();
        clicks();
    }

    private void clicks(){
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.categorias,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoCategoria.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
