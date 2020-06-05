package com.pet2u.pet2u.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;

import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Petshop.Cad_do_Pet_Activity;
import com.pet2u.pet2u.Petshop.ListagemPetshop_Activity;
import com.pet2u.pet2u.Petshop.PerfilPet_petshop;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.CadUsuario1_Activity;
import com.pet2u.pet2u.Usuario.EsqueceuSenha_Activity;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;
import com.pet2u.pet2u.modelo.Usuario;


public class MainActivity extends AppCompatActivity {

    private Button botao_entrar, botao_criar_contausu, botao_criar_contapet;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;
    private Usuario usu;
    private TextView textViewUser, loginpet, loginusu;

//    private LoginButton botao_entrarcomfacebook;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private CallbackManager mCallbackManager;
//    private AccessTokenTracker accessTokenTracker;
//    private static final String TAG = "FacebookAuthentication";
// a

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //verificarUsuarioLogado();
        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        inicializaComponentes();
        eventoClicks();

 /*       ------------ TUDO DA FUNCIONALIDADE DO FACEBOOK ------------
//

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
//    } */

        // Funcionalidade na qual lista se o switch está checked ou não (Usário ou petshop)
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

    // Método onde se encontra todos os eventos de click nos botões
    private void eventoClicks() {
        botao_criar_contausu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CadUsuario1_Activity.class);
                startActivity(i);
                limparCampos();
            }
        });


        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarUsuario();
            }
        });


        botao_criar_contapet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cad_do_Pet_Activity.class);
                startActivity(intent);
                limparCampos();
            }
        });

    }

    private void verificarUsuario(){

        String email = campoEmail.getText().toString().toLowerCase();
        String senha = campoSenha.getText().toString();
        String idUsuario = Criptografia.codificar(email);

        if (email.isEmpty() && senha.isEmpty()) {
            Toast.makeText(MainActivity.this, "Preencha os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!tipoAcesso.isChecked()) {
            DatabaseReference usuarioRef = databaseReference.child("Usuario").child(idUsuario);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        login_usuario(campoEmail.getText().toString().toLowerCase(), campoSenha.getText().toString());
                    }
                    else {
                        alert("Cadastro Não Existente");
                    }
                }

                @Override
                public void onCancelled(DatabaseError dbError) {
                    Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
                }
            };
            usuarioRef.addListenerForSingleValueEvent(eventListener);
        }

        else if (tipoAcesso.isChecked()) {
            DatabaseReference usuarioRef = databaseReference.child("Petshop").child(idUsuario);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        login_petshop(campoEmail.getText().toString().toLowerCase(), campoSenha.getText().toString());
                    }
                    else {
                        alert("Cadastro Não Existente");
                    }
                }

                @Override
                public void onCancelled(DatabaseError dbError) {
                    Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
                }
            };
            usuarioRef.addListenerForSingleValueEvent(eventListener);
        }
    };


        /*
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                  tipoUsuario = usuario.getTipoUsuario();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

//        else{
//            DatabaseReference usuarioRef2 = databaseReference.child("Petshop").child(idUsuario);
//            usuarioRef2.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Petshop petshop = dataSnapshot.getValue(Petshop.class);
//                    tipoUsuario = petshop.getTipoUsuario();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }


    private void login_petshop(String email, String senha) {
        Log.d("test", "só de flinstons");
        auth.signInWithEmailAndPassword(email.toLowerCase(), senha)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //if(auth.getCurrentUser().isEmailVerified()){
                                Intent i = new Intent(MainActivity.this, PerfilPet_petshop.class);
                                startActivity(i);
                                limparCampos();
                            //}else{
                                //alert("Por favor, verificar o seu email");
                            //}
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

     public void redefinirSenha(View view){
        Intent intent = new Intent(this, EsqueceuSenha_Activity.class);
        startActivity(intent);
        limparCampos();
    }

    private void login_usuario(String email, String senha) {
        auth.signInWithEmailAndPassword(email.toLowerCase(), senha)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //if(auth.getCurrentUser().isEmailVerified()){
                                Intent i = new Intent(MainActivity.this, ListagemPetshop_Activity.class);
                                startActivity(i);
                                limparCampos();
                            //}else{
                               // alert("Por favor, verificar o seu email");
                           // }
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
        if(auth.getCurrentUser() != null){
            Intent i = new Intent(MainActivity.this, PerfilUsuario_Activity.class);
            startActivity(i);
        }
    }

    private void limparCampos(){
        campoEmail.setText("");
        campoSenha.setText("");
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
