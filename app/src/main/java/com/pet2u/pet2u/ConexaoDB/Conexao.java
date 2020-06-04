package com.pet2u.pet2u.ConexaoDB;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Conexao {
    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference firebaseDatabase;
    private static StorageReference referenciaStorage;

    private Conexao(){

    }

    //Retorna a referencia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){

        if(firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return firebaseDatabase;
    }

    // Retorna a instancia do AUTH
    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            inicializarFireBaseAuth();
        }
        return firebaseAuth;
    }

    //Retorna a referencia do Firebase Storage
    public static StorageReference getFirebaseStorage() {
        if (referenciaStorage == null) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }

        return referenciaStorage;
    }

    //Método de logOut do usuário
    public static void logOut(){
        firebaseAuth.signOut();
    }


    private static void inicializarFireBaseAuth(){
        firebaseAuth = FirebaseAuth.getInstance();

    }

}
