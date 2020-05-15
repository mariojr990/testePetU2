package com.pet2u.pet2u.ConexaoDB;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Conexao {
    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference firebaseDatabase;

    private Conexao(){


    }

    //Retorna a instancia do FirebaseDatabase
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

    private static void inicializarFireBaseAuth(){
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public static void logOut(){
        firebaseAuth.signOut();
    }

}
