package com.pet2u.pet2u.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//       Imports FACEBOOK
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.facebook.FacebookSdk;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.pet2u.pet2u.ConexaoDB.Conexao;

import com.pet2u.pet2u.Petshop.Cad_do_Pet_Activity;
import com.pet2u.pet2u.Petshop.PerfilPet;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.CadUsuario1_Activity;
import com.pet2u.pet2u.Usuario.EsqueceuSenha_Activity;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;
import com.pet2u.pet2u.modelo.Usuario;


public class MainActivity extends AppCompatActivity {

    private Button botao_entrar, botao_criar_contausu, botao_criar_contapet;
//    private LoginButton botao_entrarcomfacebook;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;
    private Usuario usu;
    private TextView textViewUser, loginpet, loginusu;

//    private FirebaseAuth.AuthStateListener authStateListener;
//    private CallbackManager mCallbackManager;
//    private AccessTokenTracker accessTokenTracker;
//    private static final String TAG = "FacebookAuthentication";


    private FirebaseAuth auth, autenticacaopetshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verificarUsuarioLogado();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClicks();


//         ------------ TUDO DA FUNCIONALIDADE DO FACEBOOK ------------

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        botao_entrarcomfacebook = findViewById(R.id.login_button);
//        botao_entrarcomfacebook.setReadPermissions("email", "public_profile");
//        mCallbackManager = CallbackManager.Factory.create();
//        botao_entrarcomfacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "onSucess" + loginResult);
//                handleFacebookToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "onError" + error);
//            }
//        });

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(user != null){
//                    updateUI(user);
//                }else{
//                    updateUI(null);
//                }
//            }
//        };
//
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                if(currentAccessToken == null){
//                    auth.signOut();
//                }
//            }
//        };

        //    private void handleFacebookToken(AccessToken accessToken) {
//        Log.d(TAG, "handleFacebookToken" + accessToken);
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Log.d(TAG, "Login com credenciais bem sucedido");
//                    FirebaseUser user = auth.getCurrentUser();
//                    updateUI(user);
//                }else{
//                    Log.d(TAG, "Login com credenciais falhou", task.getException());
//                    alert("Autenticação falhou");
//                    updateUI(null);
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void updateUI(FirebaseUser user) {
//        if(user != null){
//            textViewUser.setText(user.getDisplayName());
//        }else{
//            textViewUser.setText("");
//        }
//    }


        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    botao_criar_contapet.setVisibility(View.VISIBLE);
                    botao_criar_contausu.setVisibility(View.INVISIBLE);
                    //botao_entrarcomfacebook.setVisibility(View.INVISIBLE);
                    loginusu.setVisibility(View.INVISIBLE);
                    loginpet.setVisibility(View.VISIBLE);
                }else {
                    botao_criar_contapet.setVisibility(View.INVISIBLE);
                    botao_criar_contausu.setVisibility(View.VISIBLE);
                    //botao_entrarcomfacebook.setVisibility(View.VISIBLE);
                    loginpet.setVisibility(View.INVISIBLE);
                    loginusu.setVisibility(View.VISIBLE);
                }
            }
        });


    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if(authStateListener != null){
//            auth.removeAuthStateListener(authStateListener);
//        }
//    }

    private void eventoClicks() {
        botao_criar_contausu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CadUsuario1_Activity.class);
                startActivity(i);
            }
        });



        
        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if ( !email.isEmpty() && !senha.isEmpty()) {
                    login(email, senha);
                }else
                {
                    Toast.makeText(MainActivity.this,
                            "Preencha os campos!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        botao_criar_contapet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cad_do_Pet_Activity.class);
                startActivity(intent);
            }
        });

    }

    public void cadastrarPetshop(View view){
        Intent intent = new Intent(this, Cad_do_Pet_Activity.class);
        startActivity(intent);
    }

    public void redefinirSenha(View view){
        Intent intent = new Intent(this, EsqueceuSenha_Activity.class);
        startActivity(intent);
        campoEmail.setText("");
        campoSenha.setText("");
    }

    private void login(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(MainActivity.this, PerfilUsuario_Activity.class);
                            startActivity(i);
                            finish();
                         }else if(tipoAcesso.isChecked() && task.isSuccessful()){
                            Intent i = new Intent(MainActivity.this, PerfilPet.class);
                            startActivity(i);
                            campoEmail.setText("");
                            campoSenha.setText("");
                        }else{
                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "E-mail ou senha não correspondem a um usuário cadastrado";
                            }catch (FirebaseAuthInvalidUserException e){
                                excecao = "Usuário não está cadastrado";
                            }catch (Exception e){
                                excecao = "Erro ao Logar: " + e.getMessage();
                                e.printStackTrace();
                            }
                            alert(excecao);
                        }
                    }
                });
    }

    private void alert(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void verificarUsuarioLogado(){
        auth = Conexao.getFirebaseAuth();
        if(auth.getCurrentUser() != null){
            Intent i = new Intent(MainActivity.this, PerfilUsuario_Activity.class);
            startActivity(i);
        }
    }

    private void inicializaComponentes(){
        botao_entrar = findViewById(R.id.EntrarLogin);
        campoEmail = findViewById(R.id.EmailLoginUsuario);
        campoSenha = findViewById(R.id.SenhaLoginUsuario);
        tipoAcesso = findViewById(R.id.UsuarioPetshop);
        botao_criar_contausu = findViewById(R.id.CriarNovaContaUsu);
        textViewUser = findViewById(R.id.txtImagem);
        botao_criar_contapet = findViewById(R.id.CriarNovaContaPet);
        loginusu = findViewById(R.id.Logintxt);
        loginpet = findViewById(R.id.LoginPet);

    }
}
