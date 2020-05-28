package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Helper.DateCustom;
import com.pet2u.pet2u.Login.MainActivity;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.CadUsuario1_Activity;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;
import com.pet2u.pet2u.domain.Address;
import com.pet2u.pet2u.domain.Util;
import com.pet2u.pet2u.domain.ZipCodeListener;
import com.pet2u.pet2u.modelo.Petshop;


public class Cad_do_Pet_Activity extends AppCompatActivity {

    private EditText campoNome, campoRazaoSocial, campoCNPJ, campoTelefone, campoSenha, campoEmail, campoCEP;
    private EditText campoCidade, campoBairro, campoEndereco, campoNumero, campoComplemento;
    private Spinner campoUF;
    private Util util;
    private Button botao_cadastroPet, botao_voltar;
    private EditText descricao_petshop;
    private FirebaseAuth auth;
    private Petshop petshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_do_pet);
        getSupportActionBar().hide();
        inicializaComponentes();
        Clicks();

        campoCEP = (EditText) findViewById(R.id.inputCepPetshop);
        campoCEP.addTextChangedListener( new ZipCodeListener(this) );

        Spinner spStates = (Spinner) findViewById(R.id.inputUfPetshop);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.states,
                        android.R.layout.simple_spinner_item);
        spStates.setAdapter(adapter);

        util = new Util(this,
                R.id.inputCepPetshop,
                R.id.inputUfPetshop,
                R.id.inputCidadePetshop,
                R.id.inputBairroPetshop,
                R.id.inputEnderecoPetshop,
                R.id.inputNumeroPetshop);
    }

    private String getZipCode(){
        return campoCEP.getText().toString();
    }

    public String getUriRequest(){
        return "https://viacep.com.br/ws/"+getZipCode()+"/json/";
    }

    public void lockFields( boolean isToLock ){
        util.lockFields( isToLock );
    }

    public void setAddressFields( Address address){
        setSpinner( R.id.inputUfPetshop, R.array.states, address.getUf() );
        setField( R.id.inputEnderecoPetshop, address.getLogradouro() );
        setField( R.id.inputComplementoPetshop, address.getComplemento() );
        setField( R.id.inputBairroPetshop, address.getBairro() );
        setField( R.id.inputCidadePetshop, address.getLocalidade() );
    }
    private void setField( int fieldId, String data ){
        ((EditText) findViewById( fieldId )).setText( data );
    }

    private void setSpinner( int fieldId, int arrayId, String uf ){
        Spinner spinner = (Spinner) findViewById( fieldId );
        String[] states = getResources().getStringArray(arrayId);

        for( int i = 0; i < states.length; i++ ){
            if( states[i].equals(uf) ){
                spinner.setSelection( i );
                break;
            }
        }
    }

    private void Clicks() {
        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        botao_cadastroPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = campoNome.getText().toString().trim();
                String razaoSocial = campoRazaoSocial.getText().toString().trim();
                String cnpj = campoCNPJ.getText().toString().trim();
                String telefone = campoTelefone.getText().toString().trim();
                String senha = campoSenha.getText().toString().trim();
                String email = campoEmail.getText().toString().trim().toLowerCase();
                String cep = campoCEP.getText().toString().trim();
                String uf = campoUF.getSelectedItem().toString();
                String cidade = campoCidade.getText().toString().trim();
                String bairro = campoBairro.getText().toString().trim();
                String endereco = campoEndereco.getText().toString().trim();
                String numero = campoNumero.getText().toString().trim();
                String complemento = campoComplemento.getText().toString().trim();
                String descricao = descricao_petshop.getText().toString().trim();

                if ( nome.isEmpty() || razaoSocial.isEmpty() || cnpj.isEmpty() || telefone.isEmpty() || senha.isEmpty() || email.isEmpty() ||
                     cep.isEmpty() || uf.isEmpty() || cidade.isEmpty() || bairro.isEmpty() || endereco.isEmpty() || numero.isEmpty() || descricao.isEmpty()){
                    alert("Preencha todos os campos!");
                }else{

                    petshop = new Petshop();
                    petshop.setNome(nome);
                    petshop.setRazao_social(razaoSocial);
                    petshop.setCnpj(cnpj);
                    petshop.setTelefone(telefone);
                    petshop.setSenha(senha);
                    petshop.setEmail(email);
                    petshop.setCep(cep);
                    petshop.setUf(uf);
                    petshop.setCidade(cidade);
                    petshop.setBairro(bairro);
                    petshop.setEndereco(endereco);
                    petshop.setNumero(numero);
                    petshop.setComplemento(complemento);
                    petshop.setDescricaoPetshop(descricao);
                    petshop.setTipoUsuario("P");

                    criarPet(petshop.getEmail(), petshop.getSenha());

                }
            }
        });


    }

    private void criarPet(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(Cad_do_Pet_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        String idPetshop = Criptografia.codificar(petshop.getEmail());
                                        petshop.setDataCadastro(DateCustom.dataAtual());
                                        petshop.setidPetshop(idPetshop);
                                        petshop.salvar();
                                        limparCampos();
                                        exibirConfirmacao();

                                    }else{
                                        alert(task.getException().getMessage());
                                    }
                                }
                            });
                        }else{
                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                excecao = "Digite uma senha mais forte!";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Por favor, digite um e-mail válido";
                            }catch (FirebaseAuthUserCollisionException e){
                                excecao = "Esta conta já foi cadastrada";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }
                            alert(excecao);

                        }
                    }
                });
    }

    private void exibirConfirmacao() {
        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(this);
        caixaDialogo.setTitle("Cadastro");
        caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
        caixaDialogo.setMessage("Sua conta foi cadastrada com sucesso, um E-mail de verificação foi enviado.");
        caixaDialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Cad_do_Pet_Activity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        caixaDialogo.show();
    }

    private void alert(String msg){
        Toast.makeText(Cad_do_Pet_Activity.this, msg,Toast.LENGTH_SHORT).show();
    }
    private void limparCampos(){
        campoNome.setText("");
        campoRazaoSocial.setText("");
        campoComplemento.setText("");
        campoSenha.setText("");
        campoEmail.setText("");
        campoUF.setAdapter(null);
        campoCidade.setText("");
        campoBairro.setText("");
        campoComplemento.setText("");
        campoEndereco.setText("");
        campoCEP.setText(null);
        campoNumero.setText(null);
        campoCNPJ.setText(null);
        campoTelefone.setText(null);

    }

    private void inicializaComponentes(){
        campoNome = findViewById(R.id.inputNomePetshop);
        campoRazaoSocial = findViewById(R.id.inputRazaoSocialPetshop2);
        campoCNPJ = findViewById(R.id.inputCnpjPetshop);
        campoTelefone = findViewById(R.id.inputTelefonePetshop);
        campoSenha = findViewById(R.id.editText_senhaPet);
        campoEmail = findViewById(R.id.inputEmail_Petshop);
        campoCEP = findViewById(R.id.inputCepPetshop);
        campoUF = findViewById(R.id.inputUfPetshop);
        campoCidade = findViewById(R.id.inputCidadePetshop);
        campoBairro = findViewById(R.id.inputBairroPetshop);
        campoEndereco = findViewById(R.id.inputEnderecoPetshop);
        campoNumero = findViewById(R.id.inputNumeroPetshop);
        campoComplemento = findViewById(R.id.inputComplementoPetshop);
        botao_cadastroPet = findViewById(R.id.botaoCadastrarPetshop);
        botao_voltar = findViewById(R.id.botaoVoltar2);
        descricao_petshop = findViewById(R.id.descreva_petshop);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
