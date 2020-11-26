package com.pet2u.pet2u.Petshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.AdapterListaPetAdocao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Pet_Adocao;

import java.util.ArrayList;
import java.util.Map;

public class lista_pet_adocao_petshop extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private ImageView image;
    private HorizontalScrollView scrollView;
    private Boolean isToolBarOpen;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private RecyclerView listaPetAdocao;
    private AdapterListaPetAdocao adapter;
    private ArrayList<Pet_Adocao> pet_adocao;
    private ArrayList<ArrayList<Pet_Adocao>> categoriasMatrix;
    private String emailCriptografado;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pet_adocao_petshop);
        getSupportActionBar().hide();

        databaseReference = Conexao.getFirebaseDatabase();
        listaPetAdocao =  findViewById(R.id.listaPetAdocao);
        image = findViewById(R.id.fotoPetAdocao);
        pet_adocao = new ArrayList<>();
        categoriasMatrix = new ArrayList<>();
        isToolBarOpen = false;
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        emailCriptografado = Criptografia.codificar(user.getEmail());

        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(emailCriptografado).child("petAdocao");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String categoriaNome = "";
                if (dataSnapshot.exists()){
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>)dataSnapshot.getValue()).entrySet()){
                        Map singleUser = (Map) entry.getValue();

                        boolean titulo = false;

                        Pet_Adocao petClicado = new Pet_Adocao();
                        petClicado.setNome((String) singleUser.get("nome"));
                        petClicado.setIdade((String) singleUser.get("idade"));
                        petClicado.setDescricaoPetAdocao((String) singleUser.get("descricao"));

                        listaPetAdocao.setLayoutManager(new LinearLayoutManager(lista_pet_adocao_petshop.this));
                        listaPetAdocao.setNestedScrollingEnabled(false);
                        adapter = new AdapterListaPetAdocao(lista_pet_adocao_petshop.this, pet_adocao);
                        listaPetAdocao.setAdapter(adapter);
                        int ViewSize = adapter.getItemCount() * 520;
                        ViewGroup.LayoutParams layoutParams = listaPetAdocao.getLayoutParams();
                        layoutParams.height = ViewSize;
                        listaPetAdocao.setLayoutParams(layoutParams);

                        adapter.setOnItemClickListener(new AdapterListaPetAdocao.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent crudPetAdocao = new Intent(lista_pet_adocao_petshop.this, crud_Pet_Adocao.class);
                                startActivity(crudPetAdocao);
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Cancelou", databaseError.getMessage());
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            int position = data.getIntExtra("position", 0);
            if (resultCode == Activity.RESULT_OK){
                pet_adocao.remove(position);
                adapter.notifyItemRemoved(position);
            }
            if (data.getBooleanExtra("updateNome", false)){
                pet_adocao.get(position).setNome(data.getStringExtra("novoNome"));
                adapter.notifyDataSetChanged();
            }
            if (data.getBooleanExtra("updateDescricao", false)) {
                pet_adocao.get(position).setDescricaoPetAdocao(data.getStringExtra("novaDescricao"));
                adapter.notifyDataSetChanged();
            }
            if (data.getBooleanExtra("updateValor", false)) {
                pet_adocao.get(position).setIdade(data.getStringExtra("novaIdade"));
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(lista_pet_adocao_petshop.this, "Erro ao editar produto!", Toast.LENGTH_SHORT);
            }
        }



















    }
}