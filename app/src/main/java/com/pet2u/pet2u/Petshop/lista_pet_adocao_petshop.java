package com.pet2u.pet2u.Petshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
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
import com.google.firebase.storage.StorageReference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.Helper.AdapterListaPetAdocao;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Pet_Adocao;

import java.util.ArrayList;

public class lista_pet_adocao_petshop extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private ImageView imagemPetshop;
    private boolean isToolbarOpen;
    private FirebaseUser user;
    private FirebaseAuth auth;


    private RecyclerView listaPetAdocao;
    private AdapterListaPetAdocao adapter;
    private ArrayList<Pet_Adocao> petAdocao;
    private ArrayList<ArrayList<Pet_Adocao>> categoriasMatrix;
    private StorageReference storageReference = Conexao.getFirebaseStorage();

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pet_adocao_petshop);


        databaseReference = conexao.getFirebaseDatabase();
        listaPetAdocao = findViewById(R.id.listaPetAdocao);
        imagemPetshop = findViewById(R.id.fotoPetAdocao);
        petAdocao = new ArrayList<>();
        categoriasMatrix = new ArrayList<>();
        isToolbarOpen = false;
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        emailCriptografado = Criptografia.codificar(user.getEmail());

        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(emailCriptografado).child("petAdocao");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean titulo = false;

                    Pet_Adocao petClicado = new Pet_Adocao();
                    petClicado.setNome((String) singleUser.get("nome"));
                    petClicado.setIdade((String) singleUser.get("idade"));
                    petClicado.getDescricaoPetAdocao((String) singleUser.get("descricao"));

                    listaPetAdocao.setLayoutManager(new LinearLayoutManager(lista_pet_adocao_petshop.this));
                    listaPetAdocao.setNestedScrollingEnabled(false);
                    adapter = new AdapterListaPetAdocao(lista_pet_adocao_petshop.this, petAdocao);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            int position = data.getIntExtra("position", 0);
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("delete", false)) {
                    petAdocao.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                if (data.getBooleanExtra("updateNome", false)) {
                    petAdocao.get(position).setNome(data.getStringExtra("novoNome"));
                    adapter.notifyDataSetChanged();
                }
                if (data.getBooleanExtra("updateDescricao", false)) {
                    petAdocao.get(position).setDescricaoPetAdocao(data.getStringExtra("novaDescricao"));
                    adapter.notifyDataSetChanged();
                }
                if (data.getBooleanExtra("updateValor", false)) {
                    petAdocao.get(position).setIdade(data.getStringExtra("novaIdade"));
                    adapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(lista_pet_adocao_petshop.this, "Erro ao editar Pet!", Toast.LENGTH_SHORT);
            }
        }
    }
}