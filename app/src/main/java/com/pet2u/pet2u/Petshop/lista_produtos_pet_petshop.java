package com.pet2u.pet2u.Petshop;

import android.animation.ObjectAnimator;
import android.app.Activity;
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
import android.widget.Toast;

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
import java.util.Objects;

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
                if (dataSnapshot.exists()) {
                    for (Map.Entry<String, Object> entry : ((Map<String,Object>)dataSnapshot.getValue()).entrySet()){

                        Map singleUser = (Map) entry.getValue();

                        Boolean titulo = false;

                        Produto produtoClicked = new Produto();
                        produtoClicked.setNome((String) singleUser.get("nome"));
                        produtoClicked.setValor((String) singleUser.get("valor"));
                        produtoClicked.setDescricaoProduto((String) singleUser.get("descricao"));
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

                    listaProdutos.setLayoutManager(new LinearLayoutManager(lista_produtos_pet_petshop.this));
                    listaProdutos.setNestedScrollingEnabled(false);
                    adapter = new AdapterListaProdutosPet(lista_produtos_pet_petshop.this, produtos);
                    listaProdutos.setAdapter(adapter);
                    int viewSize = adapter.getItemCount() * 520;
                    ViewGroup.LayoutParams layoutParams = listaProdutos.getLayoutParams();
                    layoutParams.height = viewSize;
                    listaProdutos.setLayoutParams(layoutParams);

                    adapter.setOnItemClickListener(new AdapterListaProdutosPet.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent crudproduto = new Intent(lista_produtos_pet_petshop.this, crud_Produto.class);
                            crudproduto.putExtra("nomeProduto", produtos.get(position).getNome());
                            crudproduto.putExtra("descricaoProduto", produtos.get(position).getDescricaoProduto());
                            crudproduto.putExtra("valorProduto", produtos.get(position).getValor());
                            crudproduto.putExtra("posicaoProduto", position);
                            startActivityForResult(crudproduto, 1);
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
                    produtos.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                if (data.getBooleanExtra("updateNome", false)) {
                    produtos.get(position).setNome(data.getStringExtra("novoNome"));
                    adapter.notifyDataSetChanged();
                }
                if (data.getBooleanExtra("updateDescricao", false)) {
                    produtos.get(position).setDescricaoProduto(data.getStringExtra("novaDescricao"));
                    adapter.notifyDataSetChanged();
                }
                if (data.getBooleanExtra("updateValor", false)) {
                    produtos.get(position).setValor(data.getStringExtra("novoValor"));
                    adapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(lista_produtos_pet_petshop.this, "Erro ao editar produto!", Toast.LENGTH_SHORT);
            }
        }
    }//onActivityResult

}
