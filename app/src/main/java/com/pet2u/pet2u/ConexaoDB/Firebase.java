package com.pet2u.pet2u.ConexaoDB;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

        public class Firebase {

            private static DatabaseReference referenciaFirebase;
            private static FirebaseAuth referenciaAuntenticacao;
            private static StorageReference referenciaStorage;

    public static String getIdUsuario(){
        FirebaseAuth autenticacao = getFirebaseAuntenticacao();
        return  autenticacao.getCurrentUser().getUid();
    }

    public static  DatabaseReference getFirebase() {
        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return  referenciaFirebase;

    }

    public static FirebaseAuth getFirebaseAuntenticacao() {
        if (referenciaAuntenticacao == null) {
            referenciaAuntenticacao = FirebaseAuth.getInstance();
        }

        return referenciaAuntenticacao;

    }
    public static StorageReference getFirebaseStorage() {
        if (referenciaStorage == null) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }

        return referenciaStorage;

    }

}
