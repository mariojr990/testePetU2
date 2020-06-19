package com.pet2u.pet2u.Petshop;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.AdapterListaProdutos;
import com.pet2u.pet2u.Helper.AdapterListaProdutosPet;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Produto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class lista_produtos_pet_petshop extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private ImageView imagePetshop;
    private HorizontalScrollView scrollviewTipoProdutos;
    private boolean isToolbarOpen;
    private FirebaseUser user;
    private FirebaseAuth auth;


    private RecyclerView listaProdutos;
    private AdapterListaProdutosPet adapter;
    private ArrayList<Produto> produtos;
    private ArrayList<String> categoriasList;
    private ArrayList<ArrayList<Produto>> categoriasMatrix;
    private String emailCriptografado;
    //private TabLayout tabLayout;
    private StorageReference storageReference = Conexao.getFirebaseStorage();

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos_pet_petshop);
        getSupportActionBar().hide();

        databaseReference = Conexao.getFirebaseDatabase();
        listaProdutos = findViewById(R.id.listaProdutos);
        //Button botao_voltar = findViewById(R.id.seta_voltar);
        //tabLayout = findViewById(R.id.tabLayoutToolbar);
        imagePetshop = findViewById(R.id.imageView4);
        produtos = new ArrayList<>();
        categoriasMatrix = new ArrayList<>();
        categoriasList = new ArrayList<>();
        isToolbarOpen = false;
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        emailCriptografado = Criptografia.codificar(user.getEmail());

        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(emailCriptografado).child("produto");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String categoriaNome = "";
                for (Map.Entry<String, Object> entry : ((Map<String,Object>)dataSnapshot.getValue()).entrySet()){

                    Map singleUser = (Map) entry.getValue();

                    Boolean titulo = false;

                    Produto produtoClicked = new Produto();
                    produtoClicked.setNome((String) singleUser.get("nome"));
                    produtoClicked.setValor((String) singleUser.get("valor"));
                    produtoClicked.setDescricaoProduto((String) singleUser.get("descricao"));
                    categoriaNome = (String) singleUser.get("categoria");
                    produtoClicked.setCategoria(categoriaNome);

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

                    if (!categoriasList.contains(categoriaNome)) {
                        categoriasList.add(categoriaNome);
                        titulo = true;
                    }
                    if (!categoriasList.isEmpty()) {
                        categoriasMatrix.add(new ArrayList<Produto>());
                        if (!titulo) {
                            produtoClicked.setCategoria("");
                        }
                        categoriasMatrix.get(categoriasList.indexOf(categoriaNome)).add(produtoClicked);
                    }

                }
                if (!categoriasList.isEmpty()) {
                    for (int i = 0; i < categoriasMatrix.size(); i++) {
                        for (int j = 0; j < categoriasMatrix.get(i).size(); j++) {
                            produtos.add(categoriasMatrix.get(i).get(j));
                        }
                    }
                }
//                for (String nomeCategoria : categoriasList) {
//                    tabLayout.addTab(tabLayout.newTab().setText(nomeCategoria));
//                }


                listaProdutos.setLayoutManager(new LinearLayoutManager(lista_produtos_pet_petshop.this));
                listaProdutos.setNestedScrollingEnabled(false);
                adapter = new AdapterListaProdutosPet(lista_produtos_pet_petshop.this, produtos);
                listaProdutos.setAdapter(adapter);
                int viewSize = adapter.getItemCount() * 520;
                ViewGroup.LayoutParams layoutParams = listaProdutos.getLayoutParams();
                layoutParams.height = viewSize;
                listaProdutos.setLayoutParams(layoutParams);
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                //int tamanhoToolbar = toolbarPerfilPetshop.getHeight();
//                //petshopPerfilScrollView.scrollTo(0, getRelativeTop(listaProdutos) - tamanhoToolbar - 25);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        //listaProdutos.setLayoutParams(lp);


//        botao_voltar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
}
