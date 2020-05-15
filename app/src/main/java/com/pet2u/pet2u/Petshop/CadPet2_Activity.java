package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.pet2u.pet2u.modelo.Petshop;


public class CadPet2_Activity extends AppCompatActivity {

    private EditText campoNome, campoRazaoSocial, campoCNPJ, campoTelefone, campoSenha, campoEmail, campoCEP;
    private EditText campoCidade, campoBairro, campoEndereco, campoNumero, campoComplemento, campoUF;
    private Button botao_cadastroPet, botao_voltar;
    private FirebaseAuth auth;
    private Petshop petshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_pet2);
        getSupportActionBar().hide();
        inicializaComponentes();
        Clicks();
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
                String email = campoEmail.getText().toString().trim();
                String cep = campoCEP.getText().toString().trim();
                String uf = campoUF.getText().toString().trim();
                String cidade = campoCidade.getText().toString().trim();
                String bairro = campoBairro.getText().toString().trim();
                String endereco = campoEndereco.getText().toString().trim();
                String numero = campoNumero.getText().toString().trim();
                String complemento = campoComplemento.getText().toString().trim();

                if ( nome.isEmpty() || razaoSocial.isEmpty() || cnpj.isEmpty() || telefone.isEmpty() || senha.isEmpty() || email.isEmpty() ||
                     cep.isEmpty() || uf.isEmpty() || cidade.isEmpty() || bairro.isEmpty() || endereco.isEmpty() || numero.isEmpty()){
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

                    criarPet(petshop.getEmail(), petshop.getSenha());
                    limparCampos();

                }
            }
        });


    }

    private void criarPet(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadPet2_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String idPetshop = Criptografia.codificar(petshop.getEmail());
                            petshop.setDataCadastro(DateCustom.dataAtual());
                            petshop.setidPetshop(idPetshop);
                            petshop.salvar();

                            alert("Petshop cadastrado com sucesso!");
//                            Intent i = new Intent(getApplicationContext(), PerfilPet1Activity.class);
//                            startActivity(i);
//                            finish();
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

    private void alert(String msg){
        Toast.makeText(CadPet2_Activity.this, msg,Toast.LENGTH_SHORT).show();
    }
    private void limparCampos(){
        campoNome.setText("");
        campoRazaoSocial.setText("");
        campoComplemento.setText("");
        campoSenha.setText("");
        campoEmail.setText("");
        campoUF.setText("");
        campoCidade.setText("");
        campoBairro.setText("");
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
