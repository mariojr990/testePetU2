package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
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

import java.text.Normalizer;
import java.util.Map;
import java.util.ArrayList;

public class ListagemPetshop_Activity extends AppCompatActivity {

    RecyclerView listaPetshops;
    Adapter adapter;
    ArrayList<Petshop> items;
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

                    Petshop petshopClicked = new Petshop();
                    petshopClicked.setNome((String) singleUser.get("nome"));
                    petshopClicked.setDescricaoPetshop((String) singleUser.get("descricaoPetshop"));
                    items.add(petshopClicked);
                    listaPetshops = findViewById(R.id.listasPetshop);
                    listaPetshops.setLayoutManager(new LinearLayoutManager(ListagemPetshop_Activity.this));
                    adapter = new Adapter(ListagemPetshop_Activity.this, items);
                    listaPetshops.setAdapter(adapter);

                    adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            //Log.d("xesque", "rolou o " + items.get(position).getNome());
                            Intent perfilDoPetshop = new Intent(ListagemPetshop_Activity.this, PerfilPet.class);
                            perfilDoPetshop.putExtra("nomePetshop", items.get(position).getNome());
                            perfilDoPetshop.putExtra("descricaoPetshop", items.get(position).getDescricaoPetshop());
                            startActivity(perfilDoPetshop);
                        }
                    });
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
            //
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = Normalizer.normalize(newText, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

}
