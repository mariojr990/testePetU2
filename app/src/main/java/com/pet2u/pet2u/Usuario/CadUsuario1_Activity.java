package com.pet2u.pet2u.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.pet2u.pet2u.modelo.Usuario;


public class CadUsuario1_Activity extends AppCompatActivity {

    private Button botao_cadastro, botao_voltar;
    private EditText campoNome, campoEmail, campoSenha, campoCPF, campoTelefone;
    private FirebaseAuth auth;
    private Usuario usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_usuario1);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClicks();
    }

    private void eventoClicks() {
        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botao_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString().trim().toLowerCase();
                String nome = campoNome.getText().toString().trim();
                String senha = campoSenha.getText().toString().trim();
                String cpf = campoCPF.getText().toString().trim();
                String telefone = campoTelefone.getText().toString().trim();

                if ( nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cpf.isEmpty() || telefone.isEmpty()){
                    alert("Opa! Você esqueceu de Preencher todos os campos!");
                }else{
                    if(isCPF(cpf)){
                        usu = new Usuario();
                        usu.setNome(nome);
                        usu.setEmail(email);
                        usu.setSenha(senha);
                        usu.setCPF(cpf);
                        usu.setTelefone(telefone);

                        criarUser(usu.getEmail(), usu.getSenha());
                        limparCampos();
                    }else{
                        alert("CPF inválido");
                    }

                }
            }
        });
    }



    private void criarUser(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadUsuario1_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        String idUsuario = Criptografia.codificar(usu.getEmail());
                                        usu.setDataCadastro(DateCustom.dataAtual());
                                        usu.setIdUsuario(idUsuario);
                                        usu.setTipoUsuario("U");
                                        usu.salvar();
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
        caixaDialogo.setMessage("Conta criada :D Bem vindo ao Pet2U, verifique seu E-mail!!");
        caixaDialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(CadUsuario1_Activity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        caixaDialogo.show();
    }

    private boolean isCPF(String CPF) {

        CPF = removeCaracteresEspeciais(CPF);

        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555") || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") || CPF.equals("99999999999") || (CPF.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {

                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else
                dig11 = (char) (r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return (true);
            else
                return (false);
        } catch (Exception erro) {
            erro.printStackTrace();
            return (false);
        }
    }


    private void alert(String msg){
        Toast.makeText(CadUsuario1_Activity.this, msg, Toast.LENGTH_SHORT).show();
    }


    private void limparCampos() {
        campoSenha.setText("");
        campoCPF.setText(null);
    }

    private String removeCaracteresEspeciais(String doc) {
        if (doc.contains(".")) {
            doc = doc.replace(".", "");
        }
        if (doc.contains("-")) {
            doc = doc.replace("-", "");
        }
        if (doc.contains("/")) {
            doc = doc.replace("/", "");
        }
        return doc;
    }

    private void inicializaComponentes(){
        campoNome = findViewById(R.id.inputNome);
        campoCPF = findViewById(R.id.inputCPF);
        campoTelefone = findViewById(R.id.inputTelefone);
        campoEmail = findViewById(R.id.inputEmail);
        campoSenha = findViewById(R.id.inputSenha);
        botao_cadastro = findViewById(R.id.botaoCadastrarrUsuario);
        botao_voltar = findViewById(R.id.botaoVoltarCadUsuario);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }



}
