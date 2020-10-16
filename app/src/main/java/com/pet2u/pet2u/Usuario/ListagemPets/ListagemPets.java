package com.pet2u.pet2u.Usuario.ListagemPets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.AdapterListaPets;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.Petshop.crud_Produto;
import com.pet2u.pet2u.Petshop.lista_produtos_pet_petshop;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Pets;
import com.pet2u.pet2u.modelo.Produto;

import java.util.ArrayList;
import java.util.Map;

public class ListagemPets extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private ImageView imagePetshop;
    private HorizontalScrollView scrollviewTipoProdutos;
    private boolean isToolbarOpen;
    private FirebaseUser user;
    private FirebaseAuth auth;


    private RecyclerView listaPets;
    private AdapterListaPets adapter;
    private ArrayList<Pets> pets;
    private ArrayList<String> categoriasList;
    private ArrayList<ArrayList<Pets>> categoriasMatrix;
    private String emailCriptografado;
    //private TabLayout tabLayout;
    private StorageReference storageReference = Conexao.getFirebaseStorage();

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_pets);
        getSupportActionBar().hide();

        databaseReference = Conexao.getFirebaseDatabase();
        listaPets = findViewById(R.id.listaPets);
        //Button botao_voltar = findViewById(R.id.seta_voltar);
        //tabLayout = findViewById(R.id.tabLayoutToolbar);
        //imagePetshop= findViewById(R.id.imageView4);
        pets = new ArrayList<>();
        categoriasMatrix = new ArrayList<>();
        categoriasList = new ArrayList<>();
        isToolbarOpen = false;
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        emailCriptografado = Criptografia.codificar(user.getEmail());

        DatabaseReference usuarioRef = databaseReference.child("Usuario").child(emailCriptografado).child("petUsuario");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String categoriaPet = "";
                if (dataSnapshot.exists()) {
                    for (Map.Entry<String, Object> entry : ((Map<String,Object>)dataSnapshot.getValue()).entrySet()){

                        Map singleUser = (Map) entry.getValue();

                        Boolean titulo = false;

                        Pets produtoClicked = new Pets();
                        produtoClicked.setNomePet((String) singleUser.get("nomePet"));
                        produtoClicked.setRacaPet((String) singleUser.get("racaPet"));
                        produtoClicked.setNascimentoPet((String) singleUser.get("nascimentoPet"));
                        produtoClicked.setGeneroPet((String) singleUser.get("generoPet"));
                        produtoClicked.setCastradoPet((String) singleUser.get("castradoPet"));

                        categoriaPet = (String) singleUser.get("categoriaPet");
                        produtoClicked.setCategoriaPet(categoriaPet);

//                    storageReference.child("FotoPerfilPet/" + emailCriptografado).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            Picasso.get().load(uri).fit().centerInside().into(imagePetshop);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("xesque", " A imagem não existe");
//                        }
//                    }) ;

                        if (!categoriasList.contains(categoriaPet)) {
                            categoriasList.add(categoriaPet);
                            titulo = true;
                        }
                        if (!categoriasList.isEmpty()) {
                            categoriasMatrix.add(new ArrayList<Pets>());
                            if (!titulo) {
                                produtoClicked.setCategoriaPet("");
                            }
                            categoriasMatrix.get(categoriasList.indexOf(categoriaPet)).add(produtoClicked);
                        }

                    }
                    if (!categoriasList.isEmpty()) {
                        for (int i = 0; i < categoriasMatrix.size(); i++) {
                            for (int j = 0; j < categoriasMatrix.get(i).size(); j++) {
                                pets.add(categoriasMatrix.get(i).get(j));
                            }
                        }
                    }
//                for (String nomeCategoria : categoriasList) {
//                    tabLayout.addTab(tabLayout.newTab().setText(nomeCategoria));
//                }


                    listaPets.setLayoutManager(new LinearLayoutManager(ListagemPets.this));
                    listaPets.setNestedScrollingEnabled(false);
                    adapter = new AdapterListaPets(ListagemPets.this, pets);
                    listaPets.setAdapter(adapter);
                    int viewSize = adapter.getItemCount() * 520;
                    ViewGroup.LayoutParams layoutParams = listaPets.getLayoutParams();
                    layoutParams.height = viewSize;
                    listaPets.setLayoutParams(layoutParams);

                    adapter.setOnItemClickListener(new AdapterListaPets.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {


                            Intent crudpets = new Intent(ListagemPets.this, Crud_pets.class);
                            crudpets.putExtra("nomePets", pets.get(position).getNomePet());
                            crudpets.putExtra("racaPet", pets.get(position).getRacaPet());
                            crudpets.putExtra("nascimentoPet", pets.get(position).getNascimentoPet());
                            crudpets.putExtra("generoPet", pets.get(position).getGeneroPet());
                            crudpets.putExtra("castradoPet", pets.get(position).getCastradoPet());
                            crudpets.putExtra("posicaoProduto", position);
                            startActivityForResult(crudpets, 1);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            int position = data.getIntExtra("position", 0);
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("delete", false)) {
                    pets.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                if (data.getBooleanExtra("updateNomePet", false)) {
                    pets.get(position).setNomePet(data.getStringExtra("novoNomePet"));
                    adapter.notifyDataSetChanged();
                }
                if(data.getBooleanExtra("updateRacaPet", false)){
                    pets.get(position).setRacaPet(data.getStringExtra("novaRacaPet"));
                    adapter.notifyDataSetChanged();
                }
                if(data.getBooleanExtra("updateNascimentoPet", false)){
                    pets.get(position).setNascimentoPet(data.getStringExtra("novaNascimentoPet"));
                    adapter.notifyDataSetChanged();
                }
                if(data.getBooleanExtra("updateGeneroPet", false)){
                    pets.get(position).setGeneroPet(data.getStringExtra("novaGeneroPet"));
                    adapter.notifyDataSetChanged();
                }
                if(data.getBooleanExtra("updateCastradoPet", false)){
                    pets.get(position).setCastradoPet(data.getStringExtra("novaCastradoPet"));
                    adapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(ListagemPets.this, "Erro ao editar produto!", Toast.LENGTH_SHORT);
            }
        }
    }//onActivityResult
}