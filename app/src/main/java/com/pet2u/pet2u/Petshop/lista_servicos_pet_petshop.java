package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Adapter;
import com.pet2u.pet2u.Helper.AdapterListaServicoPet;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Servico;

import java.util.ArrayList;
import java.util.Map;

public class lista_servicos_pet_petshop extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private HorizontalScrollView scrollView;
    private boolean isToolBarOpen;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private RecyclerView listarServico;
    private AdapterListaServicoPet adapter;
    private ArrayList<Servico> servicos;
    private ArrayList<String> categoriasList;
    private ArrayList<ArrayList<Servico>> categoriasMatrix;
    private String emailCriptografado;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_servicos_pet_petshop);
        getSupportActionBar().hide();

        databaseReference = Conexao.getFirebaseDatabase();
        listarServico = findViewById(R.id.listaServico);
        servicos = new ArrayList<>();
        categoriasMatrix = new ArrayList<>();
        categoriasList = new ArrayList<>();
        isToolBarOpen = false;
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        emailCriptografado = Criptografia.codificar(user.getEmail());

        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(emailCriptografado).child("servico");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String categoriaNome = "";
                if (dataSnapshot.exists()){
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>)dataSnapshot.getValue()).entrySet()){
                        Map singleUser = (Map) entry.getValue();

                        Boolean titulo = false;

                        Servico servicoClicked = new Servico();
                        servicoClicked.setNomeServico((String) singleUser.get("nomeServico"));
                        servicoClicked.setValorServico((String) singleUser.get("valorServico"));
                        servicoClicked.setDescricaoServico((String) singleUser.get("descricaoServico"));
                        categoriaNome = (String) singleUser.get("categoriaServico");
                        servicoClicked.setCategoriaServico(categoriaNome);

                        if(!categoriasList.contains(categoriaNome)){
                            categoriasList.add(categoriaNome);
                            titulo = true;
                        }
                        if(!categoriasList.isEmpty()){
                            categoriasMatrix.add(new ArrayList<Servico>());
                            if(!titulo){
                                servicoClicked.setCategoriaServico("");
                            }
                            categoriasMatrix.get(categoriasList.indexOf(categoriaNome)).add(servicoClicked);
                        }
                    }
                    if(!categoriasList.isEmpty()){
                        for(int i = 0; i < categoriasMatrix.size(); i++){
                            for(int j = 0; j < categoriasMatrix.get(i).size(); j++){
                                servicos.add(categoriasMatrix.get(i).get(j));
                            }
                        }
                    }

                    listarServico.setLayoutManager(new LinearLayoutManager(lista_servicos_pet_petshop.this));
                    listarServico.setNestedScrollingEnabled(false);
                    adapter = new AdapterListaServicoPet(lista_servicos_pet_petshop.this, servicos);
                    listarServico.setAdapter(adapter);
                    int ViewSize = adapter.getItemCount() * 520;
                    ViewGroup.LayoutParams layoutParams = listarServico.getLayoutParams();
                    layoutParams.height = ViewSize;
                    listarServico.setLayoutParams(layoutParams);

                    adapter.setOnItemClickListener(new AdapterListaServicoPet.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent crudServico = new Intent(lista_servicos_pet_petshop.this, crud_Servico.class);
//                        crudServico.putExtra("nomeServico", servicos.get(position).getNomeServico());
//                        crudServico.putExtra("nomeValor", servicos.get(position).getValorServico());
//                        crudServico.putExtra("nomeDescricao", servicos.get(position).getDescricaoServico());
//                        crudServico.putExtra("posicaoServico", position);
//                        startActivityForResult(crudServico, 1);
                            startActivity(crudServico);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            int position = data.getIntExtra("position", 0);
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("delete", false)) {
                    servicos.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                if (data.getBooleanExtra("updateNome", false)) {
                    servicos.get(position).setNomeServico(data.getStringExtra("novoNome"));
                    adapter.notifyDataSetChanged();
                }
                if (data.getBooleanExtra("updateDescricao", false)) {
                    servicos.get(position).setDescricaoServico(data.getStringExtra("novaDescricao"));
                    adapter.notifyDataSetChanged();
                }
                if (data.getBooleanExtra("updateValor", false)) {
                    servicos.get(position).setValorServico(data.getStringExtra("novoValor"));
                    adapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(lista_servicos_pet_petshop.this, "Erro ao editar produto!", Toast.LENGTH_SHORT);
            }
        }
    }
}