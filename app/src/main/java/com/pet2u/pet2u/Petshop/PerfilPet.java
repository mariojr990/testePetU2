package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.AdapterListaProdutos;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Produto;

import java.util.ArrayList;
import java.util.Map;

public class PerfilPet extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private HorizontalScrollView scrollviewTipoProdutos;
    private ConstraintLayout toolbarPerfilPetshop;
    private ScrollView petshopPerfilScrollView;
    private boolean isToolbarOpen;


    private RecyclerView listaProdutos;
    private AdapterListaProdutos adapter;
    private ArrayList<Produto> produtos;
    private String emailCriptografado;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet);

        databaseReference = Conexao.getFirebaseDatabase();
        petshopPerfilScrollView = findViewById(R.id.petshopPerfilScrollView);
        TextView nomePet = findViewById(R.id.nomePet);
        TextView enderecoPetshop = findViewById(R.id.enderecoPetshop);
        TextView tituloPetshop = findViewById(R.id.tituloPetshopToolbar);
        emailCriptografado = Criptografia.codificar(getIntent().getExtras().getString("emailPetshop"));
        Button maisInfoButton = findViewById(R.id.maisInfoButton);
        toolbarPerfilPetshop = findViewById(R.id.toolbarPerfilPetshop);
        toolbarPerfilPetshop.setVisibility(View.INVISIBLE);
        Button botao_voltar = findViewById(R.id.seta_voltar);

        produtos = new ArrayList<>();
        isToolbarOpen = false;

        nomePet.setText(getIntent().getExtras().getString("nomePetshop"));
        tituloPetshop.setText(getIntent().getExtras().getString("nomePetshop"));
        enderecoPetshop.setText(getIntent().getExtras().getString("enderecoPetshop"));

        DatabaseReference usuarioRef = databaseReference.child("Produto");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Map.Entry<String, Object> entry : ((Map<String,Object>)dataSnapshot.getValue()).entrySet()){

                    Map singleUser = (Map) entry.getValue();

                    if (((String)singleUser.get("emailPetShop")).equals(emailCriptografado)) {
                        Produto produtoClicked = new Produto();
                        produtoClicked.setNome((String) singleUser.get("nome"));
                        produtoClicked.setValor((String) singleUser.get("valor"));
                        produtos.add(produtoClicked);
                        listaProdutos = findViewById(R.id.listaProdutos);
                        listaProdutos.setLayoutManager(new LinearLayoutManager(PerfilPet.this));
                        adapter = new AdapterListaProdutos(PerfilPet.this, produtos);
                        listaProdutos.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);

        petshopPerfilScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = petshopPerfilScrollView.getScrollY();
                if (scrollY >= 630 && !isToolbarOpen) {
                    isToolbarOpen = true;
                    toolbarPerfilPetshop.post(new Runnable() {
                        @Override
                        public void run() {
                            int from = -toolbarPerfilPetshop.getWidth();
                            Log.d("xesque", "width: " + from);
                            int to = 0;
                            ObjectAnimator slider = ObjectAnimator.ofFloat(toolbarPerfilPetshop, "y", from, to);
                            slider.setDuration(400);
                            slider.start();
                            toolbarPerfilPetshop.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else if (scrollY < 630 && isToolbarOpen) {
                    isToolbarOpen = false;
                    toolbarPerfilPetshop.post(new Runnable() {
                        @Override
                        public void run() {
                            int from = 0;
                            int to = -toolbarPerfilPetshop.getWidth();
                            ObjectAnimator slider = ObjectAnimator.ofFloat(toolbarPerfilPetshop, "y", from, to);
                            slider.setDuration(600);
                            slider.start();
                            toolbarPerfilPetshop.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        });


        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
