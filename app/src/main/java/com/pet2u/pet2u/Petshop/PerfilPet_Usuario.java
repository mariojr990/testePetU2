package com.pet2u.pet2u.Petshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.AdapterListaProdutos;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;
import com.pet2u.pet2u.modelo.Produto;

import java.util.ArrayList;
import java.util.Map;

public class PerfilPet_Usuario extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private HorizontalScrollView scrollviewTipoProdutos;
    private ConstraintLayout toolbarPerfilPetshop;
    private ScrollView petshopPerfilScrollView;
    private boolean isToolbarOpen;


    private RecyclerView listaProdutos;
    private AdapterListaProdutos adapter;
    private ArrayList<Produto> produtos;
    private ArrayList<String> categoriasList;
    private ArrayList<ArrayList<Produto>> categoriasMatrix;
    private String emailCriptografado;
    private TabLayout tabLayout;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet_usuario);

        databaseReference = Conexao.getFirebaseDatabase();
        petshopPerfilScrollView = findViewById(R.id.petshopPerfilScrollView);
        TextView nomePet = findViewById(R.id.nomePet);
        TextView enderecoPetshop = findViewById(R.id.enderecoPetshop);
        final TextView tituloPetshop = findViewById(R.id.tituloPetshopToolbar);
        emailCriptografado = Criptografia.codificar(getIntent().getExtras().getString("emailPetshop"));
        listaProdutos = findViewById(R.id.listaProdutos);
        Button maisInfoButton = findViewById(R.id.maisInfoButton);
        toolbarPerfilPetshop = findViewById(R.id.toolbarPerfilPetshop);
        toolbarPerfilPetshop.setVisibility(View.INVISIBLE);
        Button botao_voltar = findViewById(R.id.seta_voltar);
        tabLayout = findViewById(R.id.tabLayoutToolbar);

        produtos = new ArrayList<>();
        categoriasMatrix = new ArrayList<>();
        categoriasList = new ArrayList<>();
        isToolbarOpen = false;

        nomePet.setText(getIntent().getExtras().getString("nomePetshop"));
        tituloPetshop.setText(getIntent().getExtras().getString("nomePetshop"));
        enderecoPetshop.setText(getIntent().getExtras().getString("enderecoPetshop"));

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
                    categoriaNome = (String) singleUser.get("categoria");
                    produtoClicked.setCategoria(categoriaNome);

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
                for (String nomeCategoria : categoriasList) {
                    tabLayout.addTab(tabLayout.newTab().setText(nomeCategoria));
                }


                listaProdutos.setLayoutManager(new LinearLayoutManager(PerfilPet_Usuario.this));
                listaProdutos.setNestedScrollingEnabled(false);
                adapter = new AdapterListaProdutos(PerfilPet_Usuario.this, produtos);
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tamanhoToolbar = toolbarPerfilPetshop.getHeight();
                scrollToView(petshopPerfilScrollView, listaProdutos);
                //petshopPerfilScrollView.scrollTo(0, getRelativeTop(listaProdutos) - tamanhoToolbar - 25);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        //listaProdutos.setLayoutParams(lp);

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
    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y - toolbarPerfilPetshop.getHeight());
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */
    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }
}
