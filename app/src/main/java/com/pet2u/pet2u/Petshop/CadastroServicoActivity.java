package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.DateCustom;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Servico_Cadastro;

import java.util.UUID;

public class CadastroServicoActivity extends AppCompatActivity {

//    private final static int CODIGO_SELECAO_FOTO = 1;
//    public String [] permissoesNecessarias = new String[]{
//            Manifest.permission.READ_EXTERNAL_STORAGE
//    };

    private Button botaoCadastrarServico, botaoVoltarServico;
    private EditText campoNomeServico, campoValorServico, campoDescricaoServico;
    private Spinner campoCategoriaServico;
    private Servico_Cadastro servico;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_servico);
        getSupportActionBar().hide();
        inicializaComponenetes();
        clicks();
    }

//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        for( int permissaoResultados : grantResults ){
////            if(permissaoResultados == PackageManager.PERMISSION_DENIED){
////                alertaValidacaoPermissao();
////            }
////        }
////    }
//    private void alertaValidacaoPermissao(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Permissões Negados");
//        builder.setMessage("Para utilizar o app é necessario aceitar as permissões");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        AlertDialog dialog=builder.create();
//        dialog.show();
//    }

    private void alert(String msg){
        Toast.makeText(CadastroServicoActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void exibirConfirmacao() {
        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(this);
        caixaDialogo.setTitle("Sucesso!");
        caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
        caixaDialogo.setMessage("Uma novidade: Seu serviço foi cadastrado com sucesso :P");
        caixaDialogo.setPositiveButton("Voltar para Perfil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
//        caixaDialogo.setNegativeButton("Cadastrar outro serviço", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(CadastroServicoActivity.this, CadastroServicoActivity.class);
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(i);
//                overridePendingTransition(0, 0);
//            }
//        });
        caixaDialogo.show();
    }

    private void clicks(){
        botaoVoltarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PerfilPet_petshop.class));
                finish();
            }
        });

        botaoCadastrarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeServiço = campoNomeServico.getText().toString().trim();
                String valorServico = campoValorServico.getText().toString().trim();
                String descricaoServico = campoDescricaoServico.getText().toString().trim();
                valorServico = valorServico.replace(".", ",");
                String categoriaServico = campoCategoriaServico.getSelectedItem().toString();

                if (nomeServiço.isEmpty() || valorServico.isEmpty()){
                    alert("Preencha todos os campos");
                }
                else{
                    servico = new Servico_Cadastro();
                    String email = Criptografia.codificar(auth.getCurrentUser().getEmail());
                    servico.setIdServico(UUID.randomUUID().toString());
                    servico.setEmailPetShopServico(email);
                    servico.setDataCadastroServico(DateCustom.dataAtual());
                    servico.setNomeServico(nomeServiço);
                    servico.setValorServico(valorServico);
                    servico.salvar("petshop", email, "servico");
                    exibirConfirmacao();
                    limparCampos();

                }
            }
        });
    }

    private void limparCampos(){
        campoNomeServico.setText("");
        campoValorServico.setText(null);
        campoDescricaoServico.setText("");
    }

    private void inicializaComponenetes(){
        campoNomeServico = findViewById(R.id.editTextNomeServico);
        campoValorServico = findViewById(R.id.editValorServico);
        campoDescricaoServico = findViewById(R.id.descricaoServico);
        botaoCadastrarServico = findViewById(R.id.botaoCadastrarServico);
        botaoVoltarServico = findViewById(R.id.botaoVoltarCadServico);
        campoCategoriaServico = findViewById(R.id.listaCategoriaServico);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.categorias_servico,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoCategoriaServico.setAdapter(adapter);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
    }
}
