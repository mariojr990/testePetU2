package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Adapter;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.Usuario.PerfilUsuario_Activity;
import com.pet2u.pet2u.modelo.Petshop;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.ArrayList;

public class ListagemPetshop_Activity extends AppCompatActivity {

    RecyclerView listaPetshops;
    Adapter adapter;
    ArrayList<Petshop> items;
    private DatabaseReference databaseReference;
    private Button button, btnAlfabetica, btnData, btnScore;
    //private Chip filtroAlfa, filtroNew, filtroScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_petshop);
        getSupportActionBar().hide();
        button = findViewById(R.id.botao_perfil_listagemPet);
        btnAlfabetica = findViewById(R.id.btnAlfabetica);
        btnData = findViewById(R.id.btnData);
        btnScore = findViewById(R.id.btnScore);

        databaseReference = Conexao.getFirebaseDatabase();
        items = new ArrayList<>();

        //sortview();

        DatabaseReference usuarioRef = databaseReference.child("Petshop");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Map.Entry<String, Object> entry : ((Map<String,Object>)dataSnapshot.getValue()).entrySet()){

                    Map singleUser = (Map) entry.getValue();

                    Petshop petshopClicked = new Petshop();
                    petshopClicked.setNome((String) singleUser.get("nome"));
                    petshopClicked.setDescricaoPetshop((String) singleUser.get("descricaoPetshop"));
                    petshopClicked.setEndereco((String) singleUser.get("endereco"));
                    petshopClicked.setEmail((String) singleUser.get("email"));
                    petshopClicked.setTelefone((String) singleUser.get("telefone"));
                    petshopClicked.setScore((String) singleUser.get("score"));
                    petshopClicked.setData_funcionamento((String) singleUser.get("data_funcionamento"));
                    petshopClicked.setHorario_funcionamento((String) singleUser.get("horario_funcionamento"));
                    petshopClicked.setComplemento((String) singleUser.get("complemento"));
                    items.add(petshopClicked);
                    listaPetshops = findViewById(R.id.listasPetshop);
                    listaPetshops.setHasFixedSize(false);
                    listaPetshops.setLayoutManager(new LinearLayoutManager(ListagemPetshop_Activity.this));
                    //Log.d("a", (String)dataSnapshot.getKey());
                    adapter = new Adapter(ListagemPetshop_Activity.this, items);
                    listaPetshops.setAdapter(adapter);

                    adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.d("xesque", "rolou o " + items.get(position).getNome());
                            Intent perfilDoPetshop = new Intent(ListagemPetshop_Activity.this, PerfilPet_Usuario.class);
                            perfilDoPetshop.putExtra("nomePetshop", items.get(position).getNome());
                            perfilDoPetshop.putExtra("descricaoPetshop", items.get(position).getDescricaoPetshop());
                            perfilDoPetshop.putExtra("enderecoPetshop", items.get(position).getEndereco());
                            perfilDoPetshop.putExtra("emailPetshop", items.get(position).getEmail());
                            perfilDoPetshop.putExtra("telefonePetshop", items.get(position).getTelefone());
                            perfilDoPetshop.putExtra("dataFuncionamento", items.get(position).getData_funcionamento());
                            perfilDoPetshop.putExtra("horarioFuncionamento", items.get(position).getHorario_funcionamento());
                            perfilDoPetshop.putExtra("complementoEnderecoPetshop", items.get(position).getComplemento());
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PerfilUsuario_Activity.class));
            }
        });

        btnAlfabetica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(items, Petshop.ByAlfabetica);
                adapter.notifyDataSetChanged();

            }
        });

        btnData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//              Collections.sort(items, Petshop.ByData);
//              adapter.notifyDataSetChanged();
                Intent i = new Intent(ListagemPetshop_Activity.this, ListagemPetshop_Activity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(items, Petshop.ByScore);
                adapter.notifyDataSetChanged();
            }
        });


    }
}