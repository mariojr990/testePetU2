package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;

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
                    produto.setMarca(marca);
                    produto.setValor(valor);
                    produto.salvar();
                    //exibirConfirmacao();
                    alert("Produto Cadastrado com sucesso!");
                    limparCampos();


                }
            }
        });
    }

    private void alert(String msg){
        Toast.makeText(CadastroProdutoActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

//    private void exibirConfirmacao() {
//        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(this);
//        caixaDialogo.setTitle("Cadastro");
//        caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
//        caixaDialogo.setMessage("Sua conta foi cadastrada com sucesso, um E-mail de verificação foi enviado.");
//        caixaDialogo.setPositiveButton("Voltar para Perfil", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(CadastroProdutoActivity.this, PerfilPet1Activity.class);
//                startActivity(i);
//                finish();
//            }
//        });
//        caixaDialogo.setNegativeButton("Cadastrar outro produto", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        caixaDialogo.show();
//    }

    private void limparCampos(){
        campoNomeProduto.setText("");
        campoValorProduto.setText(null);
        campoMarcaProduto.setText("");
    }

    private void inicializaComponenetes(){
        campoNomeProduto = findViewById(R.id.editTextNomeProduto);
        campoMarcaProduto = findViewById(R.id.editTextMarcaProduto);
        campoValorProduto = findViewById(R.id.editTextValorProduto);
        botaoVoltar = findViewById(R.id.botaoVoltarCadProduto);
        botaoCadastrarProduto = findViewById(R.id.botaoCadastrarProduto);
        campoCategoria = findViewById(R.id.ListaCategoria);

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
