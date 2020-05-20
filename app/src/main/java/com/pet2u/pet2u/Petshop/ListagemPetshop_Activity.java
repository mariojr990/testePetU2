package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Adapter;
import com.pet2u.pet2u.Login.MainActivity;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;

import java.util.Map;
import java.util.ArrayList;

public class ListagemPetshop_Activity extends AppCompatActivity {

    RecyclerView listaPetshops;
    Adapter adapter;
    ArrayList<String> items;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_petshop);
        getSupportActionBar().hide();

        databaseReference = Conexao.getFirebaseDatabase();
        items = new ArrayList<>();

        DatabaseReference usuarioRef = databaseReference.child("Petshop");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Map.Entry<String, Object> entry : ((Map<String,Object>)dataSnapshot.getValue()).entrySet()){

                    Map singleUser = (Map) entry.getValue();
                    //items.add((String) singleUser.get("nome"));
                    items.add((String) singleUser.get("nome"));
                    listaPetshops = findViewById(R.id.listasPetshop);
                    listaPetshops.setLayoutManager(new LinearLayoutManager(ListagemPetshop_Activity.this));
                    adapter = new Adapter(ListagemPetshop_Activity.this, items);
                    listaPetshops.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);


        SearchView searchView = findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

}
